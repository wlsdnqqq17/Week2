package com.example.week2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.week2.data.AppRoomDatabase
import com.example.week2.data.item.Item
import com.example.week2.data.item.ItemRepository
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var repository: ItemRepository
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        apiService = RetrofitClient.getInstance().create(ApiService::class.java)

        val db = AppRoomDatabase.getDatabase(applicationContext, CoroutineScope(Dispatchers.IO))
        repository = ItemRepository(db.itemDao())

        // Check if the user is already logged in
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val savedLoginId = sharedPref.getString("login_id", null)
        val savedNickname = sharedPref.getString("nickname", null)

        if (savedLoginId != null && savedNickname != null) {
            // User is already logged in, redirect to HomePageActivity
            val intent = Intent(this, HomePageActivity::class.java)
            startActivity(intent)
            finish() // Prevent the user from returning to the login screen
            return
        }

        val kakaoLoginButton: ImageButton = findViewById(R.id.kakao_login_button)
        /*------For Test-------*/
        //val intent = Intent(this@MainActivity, HomePageActivity::class.java)
        //startActivity(intent)
        /*------For Test-------*/
        kakaoLoginButton.setOnClickListener {
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e("KakaoLogin", "로그인 실패", error)
                    } else if (token != null) {
                        Log.i("KakaoLogin", "로그인 성공")
                        UserApiClient.instance.me { user, error ->
                            if (error != null) {
                                Log.e("KakaoLogin", "사용자 정보 요청 실패", error)
                            } else if (user != null) {
                                val loginId = user.id.toString()
                                val nickname = user.kakaoAccount?.profile?.nickname
                                if (nickname != null) {
                                    saveUserInfo(loginId, nickname)
                                    sendUserToServer(loginId, nickname)
                                }
                            }
                        }
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                    if (error != null) {
                        Log.e("KakaoLogin", "로그인 실패", error)
                    } else if (token != null) {
                        Log.i("KakaoLogin", "로그인 성공")
                        UserApiClient.instance.me { user, error ->
                            if (error != null) {
                                Log.e("KakaoLogin", "사용자 정보 요청 실패", error)
                            } else if (user != null) {
                                val loginId = user.id.toString()
                                val nickname = user.kakaoAccount?.profile?.nickname
                                if (nickname != null) {
                                    saveUserInfo(loginId, nickname)
                                    sendUserToServer(loginId, nickname)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun saveUserInfo(loginId: String, nickname: String) {
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("login_id", loginId)
            putString("nickname", nickname)
            apply()
        }
    }

    private fun sendUserToServer(loginId: String, nickname: String) {
        val retrofit = RetrofitClient.getInstance()
        val apiService = retrofit.create(ApiService::class.java)
        val user = User(loginId, nickname)

        Log.i("SendUserToServer", "Sending user data: $loginId, $nickname")

        apiService.saveKakaoUser(user).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.i("ServerResponse", "User data sent successfully")
                    fetchUserItems(loginId)
                } else {
                    Log.e("ServerResponse", "Failed to send user data. Error code: ${response.code()}, Error body: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ServerResponse", "Error: ${t.message}")
            }
        })
    }

    private fun fetchUserItems(loginId: String) {
        apiService.getUserItems(loginId).enqueue(object : Callback<List<Item>> {
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                if (response.isSuccessful) {
                    val userItems = response.body()
                    if (userItems != null) {
                        resetLocalDatabase(userItems)
                    }
                    val intent = Intent(this@MainActivity, HomePageActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.e("FetchUserItems", "Failed to fetch user items. Error code: ${response.code()}, Error body: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                Log.e("FetchUserItems", "Error: ${t.message}")
            }
        })
    }

    private fun resetLocalDatabase(items: List<Item>) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteAll()
            Log.d("ResetDB", "All items deleted from the local database")
            repository.insertAll(items)
            Log.d("StoreActivity", "User items have been saved to the local database")
            fetchShopItems()
        }
    }

    private fun fetchShopItems() {
        apiService.getShopItems().enqueue(object : Callback<List<ServerItem>> {
            override fun onResponse(call: Call<List<ServerItem>>, response: Response<List<ServerItem>>) {
                if (response.isSuccessful) {
                    val items = response.body()
                    items?.let {
                        Log.d("ShopItems", "JSON에서 받아온 items: $it")
                        updateLocalDatabase(it)
                    }
                }
            }

            override fun onFailure(call: Call<List<ServerItem>>, t: Throwable) {
                Log.e("ShopItems", "실패!!", t)
            }
        })
    }

    private fun updateLocalDatabase(serverItems: List<ServerItem>) {
        CoroutineScope(Dispatchers.IO).launch {
            val items = serverItems.map { it.toItem() }
            val existingItems = mutableListOf<Item>()
            repository.allItems.collect {
                existingItems.addAll(it)
                Log.d("ShopItems", "Collected existing items: $existingItems")

                val itemsToUpdate = mutableListOf<Item>()
                val itemsToInsert = mutableListOf<Item>()

                items.forEach() { newItem ->
                    val existingItem = existingItems.find { it.id == newItem.id }
                    if (existingItem != null) {
                        itemsToUpdate.add(newItem.copy(isPurchased = existingItem.isPurchased))
                    } else {
                        itemsToInsert.add(newItem)
                    }
                }
                if (itemsToUpdate.isNotEmpty()) {
                    repository.updateAll(itemsToUpdate)
                    Log.d("ShopItems", "Updated items: $itemsToUpdate")
                }
                if (itemsToInsert.isNotEmpty()) {
                    repository.insertAll(itemsToInsert)
                    Log.d("ShopItems", "Inserted items: $itemsToInsert")
                }
                repository.allItems.collect { allItems ->
                    Log.d("ShopItems", "After Updating: $allItems")
                }
            }
        }
    }

    private fun ServerItem.toItem(): Item {
        return Item(
            id = this.id,
            name = this.name,
            category = this.category,
            item_image_url = this.item_image_url,
            price = this.price,
            isPurchased = false // 초기값 설정
        )
    }
}
