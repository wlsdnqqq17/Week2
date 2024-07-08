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

    @Delete
    suspend fun delete(item: Item)

}
