package com.example.week2

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.week2.DB.ApiClient
import com.example.week2.data.AppRoomDatabase
import com.example.week2.data.item.Item
import com.example.week2.data.item.ItemRepository
import com.example.week2.data.item.ItemViewModel
import com.example.week2.data.item.ItemViewModelFactory
import com.example.week2.data.meal.WordsApplication
import com.example.week2.ui.adapter.ItemListAdapter
import com.example.week2.ui.adapter.MealListAdapter
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

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
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }


    private fun fetchShopItems() {
        val url = "https://run.mocky.io/v3/3b211402-549c-4d43-b22a-9437e3bbde58/"
        apiService.getShopItems(url).enqueue(object : Callback<List<Item>> {
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                Log.d("ShopItems", "ShopItems: $response")

                if (response.isSuccessful) {
                    val items = response.body()
                    items?.let {
                        Log.d("ShopItems", "ShopItems: $it")
                        saveItemsToLocalDatabase(it)
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

    private fun saveItemsToLocalDatabase(items: List<Item>) {
        CoroutineScope(Dispatchers.IO).launch {

            repository.deleteAll()

            repository.insertAll(items)

            repository.allItems.collect { allItems ->
                Log.d("ShopItems", "After Inserting: $allItems")
            }
        }
    }

    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }
}
