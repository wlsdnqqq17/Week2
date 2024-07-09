package com.example.week2.data.item

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM item_table")
    fun getAllItems(): Flow<List<Item>>

    @Insert
    suspend fun insert(item: Item)

    @Insert
    suspend fun insertAll(items: List<Item>)

    @Update
    suspend fun update(item: Item)

    @Update
    suspend fun updateAll(items: List<Item>)

    @Delete
    suspend fun delete(item: Item)

    @Query("DELETE FROM item_table")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<Item>)
}
