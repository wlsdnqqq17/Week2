package com.example.week2.data.item

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class ItemRepository(private val itemDao: ItemDao) {

    val allItems: Flow<List<Item>> = itemDao.getAllItems()

    fun getPurchasedItemsByCategory(category: String): Flow<List<Item>> {
        return itemDao.getPurchasedItemsByCategory(category)
    }

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
    suspend fun updateAll(items: List<Item>) {
        itemDao.updateAll(items)
    }

    @WorkerThread
    suspend fun delete(item: Item) {
        itemDao.delete(item)
    }

    @WorkerThread
    suspend fun deleteAll(items: List<Item>) {
        itemDao.updateAll(items)
    }

    @WorkerThread
    suspend fun deleteAll() {
        itemDao.deleteAll()
    }

    @WorkerThread
    suspend fun getItemById(itemId: Int): Item? {
        return itemDao.getItemById(itemId)
    }

    @WorkerThread
    suspend fun getAllItems(): Flow<List<Item>> {
        return itemDao.getAllItems()
    }
}
