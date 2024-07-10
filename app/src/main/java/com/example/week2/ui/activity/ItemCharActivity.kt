package com.example.week2

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.week2.data.item.Item
import com.example.week2.data.item.ItemViewModel
import com.example.week2.data.item.ItemViewModelFactory
import com.example.week2.data.meal.WordsApplication
import com.example.week2.ui.adapter.ItemListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemCharActivity : AppCompatActivity(), ItemListAdapter.OnItemClickListener {

    private val itemViewModel: ItemViewModel by viewModels {
        ItemViewModelFactory((application as WordsApplication).itemRepository)
    }
    private val adapter = ItemListAdapter(this, true)
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)
        val toolbar_title: TextView = findViewById(R.id.toolbar_title)
        toolbar_title.text = "캐릭터"

        sharedPreferences = getSharedPreferences("Items", MODE_PRIVATE)
        apiService = RetrofitClient.getInstance().create(ApiService::class.java)
        val button = findViewById<Button>(R.id.default_button)
        button.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putInt("clothes", 0)
            editor.apply()
            val savedClothesId = sharedPreferences.getInt("clothes", 0)
            Log.d("ItemCharActivity", "Saved clothes ID: $savedClothesId")
            adapter.selectedPosition = RecyclerView.NO_POSITION
            adapter.notifyDataSetChanged()
        }
        setupToolbar()
        setupRecyclerView()

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

    private fun observeViewModel() {
        itemViewModel.getPurchasedItemsByCategory("clothes").observe(this) { items ->
            items.let {
                adapter.submitList(it)
                updateSelectedItem(it)
            }
        }
    }

    private fun updateSelectedItem(items: List<Item>) {
        val savedClothesId = sharedPreferences.getInt("clothes", -1)
        val selectedItemPosition = items.indexOfFirst { it.id == savedClothesId }
        if (selectedItemPosition != -1) {
            adapter.selectedPosition = selectedItemPosition
            adapter.notifyItemChanged(selectedItemPosition)
        }
    }

    override fun onItemClick(position: Int) {
        val selectedItem = adapter.currentList[position]
        saveClothesIdToSharedPreferences(selectedItem.id)

        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val login_id = sharedPref.getString("login_id", "") ?: ""
        updateAvatarState(login_id, selectedItem.id)

        adapter.notifyItemChanged(position)
    }

    private fun saveClothesIdToSharedPreferences(clothesId: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("clothes", clothesId)
        editor.apply()
        val savedClothesId = sharedPreferences.getInt("clothes", -1)
        Log.d("ItemCharActivity", "Saved clothes ID: $savedClothesId")
    }

    private fun updateAvatarState(userId: String, itemId: Int) {
        val request = UpdateAvatarStateRequest(userId, itemId)
        apiService.updateAvatarState(request).enqueue(object : Callback<UpdateAvatarStateResponse> {
            override fun onResponse(call: Call<UpdateAvatarStateResponse>, response: Response<UpdateAvatarStateResponse>) {
                if (response.isSuccessful) {
                    val status = response.body()?.status
                    Log.d("UpdateAvatarState", "Status: $status")
                } else {
                    Log.e("UpdateAvatarState", "Failed to update avatar state. Error code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UpdateAvatarStateResponse>, t: Throwable) {
                Log.e("UpdateAvatarState", "Error: ${t.message}")
            }
        })
    }
}
