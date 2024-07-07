package com.example.week2

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {
    @Query("SELECT * FROM meal_table WHERE date = :date")
    fun getMealsByDate(date: String): Flow<List<Meal>>

    @Insert
    suspend fun insert(meal: Meal)

    @Update
    suspend fun update(meal: Meal)

    @Delete
    suspend fun delete(meal: Meal)
}
