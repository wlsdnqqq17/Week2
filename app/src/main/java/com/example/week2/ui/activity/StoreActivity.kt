package com.example.week2

import android.content.Context
import android.os.Bundle
import android.content.SharedPreferences
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.week2.DB.ApiClient
import com.example.week2.data.AppRoomDatabase
import com.example.week2.data.item.Item
import com.example.week2.data.item.ItemRepository
import com.example.week2.data.item.ItemViewModel
import com.example.week2.data.item.ItemViewModelFactory
import com.example.week2.data.meal.WordsApplication
import com.example.week2.ui.adapter.ItemListAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreActivity : AppCompatActivity(), ItemListAdapter.OnItemClickListener{
    private lateinit var apiService: ApiService
    private lateinit var repository: ItemRepository
    private val itemViewModel: ItemViewModel by viewModels {
        ItemViewModelFactory((application as WordsApplication).itemRepository)
    }
    private val adapter = ItemListAdapter(this)
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        sharedPreferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE)

        setupToolbar()
        setupRecyclerView()
        apiService = ApiClient.getClient().create(ApiService::class.java)

        val db = AppRoomDatabase.getDatabase(applicationContext, CoroutineScope(Dispatchers.IO))
        repository = ItemRepository(db.itemDao())
        fetchShopItems()
        observeViewModel()
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerview)
        recyclerView.adapter = adapter
        val spanCount = 2 // 한 줄에 표시할 항목 수
        recyclerView.layoutManager = GridLayoutManager(this, spanCount)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }


    private fun fetchShopItems() {
        val url = "https://run.mocky.io/v3/3b211402-549c-4d43-b22a-9437e3bbde58/"
        apiService.getShopItems(url).enqueue(object : Callback<List<Item>> {
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {

                if (response.isSuccessful) {
                    val items = response.body()
                    items?.let {
                        Log.d("ShopItems", "JSON에서 받아온 items: $it")
                        updateLocalDatabase(it)
                    }
                }
            }

            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                Log.e("ShopItems", "실패!!", t)
            }
        })
    }
    private fun observeViewModel() {
        itemViewModel.allItems.observe(this) { items ->
            items.let { adapter.submitList(it) }
        }
    }

    private fun updateLocalDatabase(items: List<Item>) {
        CoroutineScope(Dispatchers.IO).launch {
            val existingItems = mutableListOf<Item>()

            // 데이터를 수집하고 로그를 출력합니다.
            repository.allItems.collect {
                existingItems.addAll(it)
                Log.d("ShopItems", "Collected existing items: $existingItems")

                // 서버에서 가져온 데이터와 기존 데이터를 병합합니다.
                val itemsToUpdate = items.map { newItem ->
                    val existingItem = existingItems.find { it.id == newItem.id }
                    if (existingItem != null) {
                        newItem.copy(isPurchased = existingItem.isPurchased)
                    } else {
                        newItem
                    }
                }

                // 로컬 데이터베이스에 업데이트된 데이터를 저장합니다.
                repository.updateAll(itemsToUpdate)

                // 업데이트 후 모든 데이터를 로그로 출력합니다.
                repository.allItems.collect { allItems ->
                    Log.d("ShopItems", "After Updating: $allItems")
                }
            }
        }
    }



    override fun onItemClick(position: Int) {
        val selectedItem = adapter.currentList[position]
        saveItemIdToPreferences(selectedItem.id)
    }

    private fun saveItemIdToPreferences(itemId: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("selected_item_id", itemId)
        editor.apply()
        Log.d("StoreActivity", "Item ID $itemId saved to SharedPreferences")
    }
}
