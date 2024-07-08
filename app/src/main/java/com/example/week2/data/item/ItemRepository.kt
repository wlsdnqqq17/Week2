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
    suspend fun insertAll(items: List<Item>) {
        itemDao.insertAll(items)
    }

    @WorkerThread
    suspend fun update(item: Item) {
        itemDao.update(item)
    }

    @WorkerThread
    suspend fun delete(item: Item) {
        itemDao.delete(item)
    }

    suspend fun deleteAll() {
        itemDao.deleteAll()
    }

    suspend fun insertItems(items: List<Item>) {
        itemDao.insertItems(items)
    }
}
