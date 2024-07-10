package com.example.week2.data.item

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM item_table")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * FROM item_table WHERE id = :itemId")
    suspend fun getItemById(itemId: Int): Item?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Item>)

    @Update
    suspend fun update(item: Item)

    @Update
    suspend fun updateAll(items: List<Item>)

    @Delete
    suspend fun delete(item: Item)

    @Query("DELETE FROM item_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM item_table WHERE is_purchased = 1 AND category = :category")
    fun getPurchasedItemsByCategory(category: String): Flow<List<Item>>

    @Query("SELECT * FROM item_table WHERE category = :category")
    fun getItemsByCategory(category: String): Flow<List<Item>>
}
