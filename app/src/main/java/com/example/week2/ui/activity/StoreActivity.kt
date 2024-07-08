package com.example.week2

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.week2.DB.ApiClient
import com.example.week2.data.AppRoomDatabase
import com.example.week2.data.item.Item
import com.example.week2.data.item.ItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class StoreActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService
    private lateinit var repository: ItemRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        apiService = RetrofitClient.getInstance().create(ApiService::class.java)

        val db = AppRoomDatabase.getDatabase(applicationContext, CoroutineScope(Dispatchers.IO))
        repository = ItemRepository(db.itemDao())
        fetchShopItems()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }


    private fun fetchShopItems() {
        apiService.getShopItems().enqueue(object : Callback<List<Item>> {
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                Log.d("ShopItems", "ShopItems: $response")

                if (response.isSuccessful) {
                    val items = response.body()
                    items?.let {
                        Log.d("ShopItems", "ShopItems: $it")
                        saveItemsToLocalDatabase(it)
                    }
                }else{
                    Log.e("ShopItems", "응답 실패: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                Log.e("ShopItems", "실패!!", t)
            }
        })
    }
    private fun saveItemsToLocalDatabase(items: List<Item>) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteAll()
            val itemsWithDefaults = items.map { item ->
                item.copy(
                    imageUri = item.imageUri ?: ""
                )
            }
            repository.insertAll(itemsWithDefaults)
            repository.allItems.collect { allItems ->
                Log.d("ShopItems", "After Inserting: $allItems")
            }
        }
    }
}
