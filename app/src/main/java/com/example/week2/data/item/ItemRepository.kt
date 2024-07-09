package com.example.week2.data.item

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class ItemRepository(private val itemDao: ItemDao) {

    val allItems: Flow<List<Item>> = itemDao.getAllItems()

    @WorkerThread
    suspend fun insert(item: Item) {
        itemDao.insert(item)
    }

    @WorkerThread
    suspend fun update(item: Item) {
        itemDao.update(item)
    }

    @WorkerThread
    suspend fun updateAll(items: List<Item>) {
        itemDao.updateAll(items)
    }


    @WorkerThread
    suspend fun getItemById(itemId: Int): Item? {
        return itemDao.getItemById(itemId)
    }

}
