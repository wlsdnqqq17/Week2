package com.example.week2.data.item

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_table")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "image_uri") val item_image_url: String?,
    @ColumnInfo(name = "price") val price: Int,
    @ColumnInfo(name = "is_purchased") val isPurchased: Boolean = false
)
