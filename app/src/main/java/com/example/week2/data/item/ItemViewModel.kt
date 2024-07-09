package com.example.week2.data.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ItemViewModel(private val repository: ItemRepository) : ViewModel() {

    val allItems: LiveData<List<Item>> = repository.allItems.asLiveData()

    fun getPurchasedItemsByCategory(category: String): LiveData<List<Item>> {
        return repository.getPurchasedItemsByCategory(category).asLiveData()
    }

    fun insert(item: Item) = viewModelScope.launch {
        repository.insert(item)
    }

    fun getItemById(itemId: Int): LiveData<Item?> = liveData {
        emit(repository.getItemById(itemId))
    }

}

class ItemViewModelFactory(private val repository: ItemRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ItemViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}