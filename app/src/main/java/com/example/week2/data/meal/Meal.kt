package com.example.week2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "meal_table", indices = [Index(value = ["date"])])
data class Meal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "meal_time") val mealTime: String,
    @ColumnInfo(name = "meal_name") val mealName: String,
    @ColumnInfo(name = "price") val price: Int,
    @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "memo") val memo: String? = null
)
