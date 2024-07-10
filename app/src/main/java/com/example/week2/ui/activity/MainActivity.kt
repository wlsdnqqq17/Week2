package com.example.week2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.week2.data.AppRoomDatabase
import com.example.week2.data.item.Item
import com.example.week2.data.item.ItemRepository
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
        //val intent = Intent(this@MainActivity, HomePageActivity::class.java)
        //startActivity(intent)

        apiService = RetrofitClient.getInstance().create(ApiService::class.java)

        val db = AppRoomDatabase.getDatabase(applicationContext, CoroutineScope(Dispatchers.IO))
        repository = ItemRepository(db.itemDao())

        // Check if the user is already logged in
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val savedLoginId = sharedPref.getString("login_id", null)
        val savedNickname = sharedPref.getString("nickname", null)

        if (savedLoginId != null && savedNickname != null) {
            // User is already logged in, redirect to HomePageActivity
            fetchUserItems(savedLoginId)
            fetchAvatarState(savedLoginId) {
                gotoHomePage()
            }
            return
        }


        val videoView: VideoView = findViewById(R.id.splash_video_view)
        val kakaoLoginButton: ImageButton = findViewById(R.id.kakao_login_button)
        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.splash)
        videoView.setVideoURI(videoUri)
        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
        }
        videoView.start()
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
                                    sendUserToServer(loginId, nickname) {
                                        fetchAvatarState(loginId) {
                                            gotoHomePage()
                                        }
                                    }
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
                                    sendUserToServer(loginId, nickname) {
                                        fetchAvatarState(loginId) {
                                            gotoHomePage()
                                        }
                                    }
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

    private fun sendUserToServer(loginId: String, nickname: String, onComplete: () -> Unit) {
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
                onComplete()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ServerResponse", "Error: ${t.message}")
                onComplete()
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

    private fun fetchAvatarState(userId: String, onComplete: () -> Unit) {
        apiService.getAvatarState(userId).enqueue(object : Callback<AvatarStateResponse> {
            override fun onResponse(call: Call<AvatarStateResponse>, response: Response<AvatarStateResponse>) {
                if (response.isSuccessful) {
                    val avatarStateResponse = response.body()
                    if (avatarStateResponse != null && avatarStateResponse.avatar_state != null) {
                        Log.d("fetchAvatarState", "Parsed response: ${avatarStateResponse.avatar_state}")
                        saveAvatarStateToSharedPreferences(avatarStateResponse.avatar_state)

                        val sharedPreferencesItems: SharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
                        // 로그 확인
                        val accId = sharedPreferencesItems.getInt("accessory", 0)
                        val bgId = sharedPreferencesItems.getInt("background", 0)
                        val charId = sharedPreferencesItems.getInt("clothes", 0)
                        val hatId = sharedPreferencesItems.getInt("hat", 0)

                        Log.d("fetchAvatarState", "Accessory ID: $accId, Background ID: $bgId, Clothes ID: $charId, Hat ID: $hatId")
                        // 로그 확인
                    } else {
                        Log.e("FetchAvatarState", "Avatar state is null")
                    }
                } else {
                    Log.e("FetchAvatarState", "Failed to fetch avatar state. Error code: ${response.code()}, Error body: ${response.errorBody()?.string()}")
                }
                onComplete()
            }

            override fun onFailure(call: Call<AvatarStateResponse>, t: Throwable) {
                Log.e("FetchAvatarState", "Error: ${t.message}")
                onComplete()
            }
        })
    }

    private fun saveAvatarStateToSharedPreferences(avatarState: AvatarState) {
        Log.d("saveAvatarStateToSharedPreference", "$avatarState")
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("hat", avatarState.hat_item_id ?: 0)
            putInt("clothes", avatarState.clothes_item_id ?: 0)
            putInt("accessory", avatarState.accessory_item_id ?: 0)
            putInt("background", avatarState.background_item_id ?: 0)
            apply()
        }
        //로그확인
        val sharedPreferencesItems: SharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val accId = sharedPreferencesItems.getInt("accessory", 0)
        val bgId = sharedPreferencesItems.getInt("background", 0)
        val charId = sharedPreferencesItems.getInt("clothes", 0)
        val hatId = sharedPreferencesItems.getInt("hat", 0)

        Log.d("saveAvatarStateToSharedPreference", "Accessory ID: $accId, Background ID: $bgId, Clothes ID: $charId, Hat ID: $hatId")
        //로그확인
    }

    private fun gotoHomePage() {
        val intent = Intent(this@MainActivity, HomePageActivity::class.java)
        startActivity(intent)
        finish()
    }
}
