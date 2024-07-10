package com.example.week2

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
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

class ItemHatActivity : AppCompatActivity(), ItemListAdapter.OnItemClickListener {

    private val itemViewModel: ItemViewModel by viewModels {
        ItemViewModelFactory((application as WordsApplication).itemRepository)
    }
    private val adapter = ItemListAdapter(this, true)
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)
        sharedPreferences = getSharedPreferences("Items", MODE_PRIVATE)

        val button = findViewById<Button>(R.id.default_button)
        button.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putInt("hat", 0)
            editor.apply()
            val savedHatId = sharedPreferences.getInt("hat", 0)
            Log.d("ItemHatActivity", "Saved hat ID: $savedHatId")
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
        itemViewModel.getPurchasedItemsByCategory("hat").observe(this) { items ->
            items.let {
                adapter.submitList(it)
                updateSelectedItem(it)
            }
        }
    }

    private fun updateSelectedItem(items: List<Item>) {
        val savedHatId = sharedPreferences.getInt("hat", -1)
        val selectedItemPosition = items.indexOfFirst { it.id == savedHatId }
        if (selectedItemPosition != -1) {
            adapter.selectedPosition = selectedItemPosition
            adapter.notifyItemChanged(selectedItemPosition)
        }
    }

    override fun onItemClick(position: Int) {
        val selectedItem = adapter.currentList[position]
        saveHatIdToSharedPreferences(selectedItem.id)
        adapter.notifyItemChanged(position)
    }

    private fun saveHatIdToSharedPreferences(hatId: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt("hat", hatId)
        editor.apply()
        val savedHatId = sharedPreferences.getInt("hat", -1)
        Log.d("ItemHatActivity", "Saved Hat ID: $savedHatId")
    }
}
