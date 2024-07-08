package com.example.week2

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        val mealIcon: ImageView = findViewById(R.id.meal_icon)
        val inventoryIcon: ImageView = findViewById(R.id.inventory)
        val calendarIcon: ImageView = findViewById(R.id.calendar_icon)
        val storeIcon: ImageView = findViewById(R.id.store_icon)
        val friendIcon: ImageView = findViewById(R.id.friend_icon)
        val potatoIcon: ImageView = findViewById(R.id.potato_icon)
        val infoIcon: ImageView = findViewById(R.id.info)

        mealIcon.setOnClickListener {
            val intent = Intent(this, MealActivity::class.java)
            startActivity(intent)
        }

        inventoryIcon.setOnClickListener {
            val intent = Intent(this, InventoryActivity::class.java)
            startActivity(intent)
        }

        calendarIcon.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        storeIcon.setOnClickListener {
            val intent = Intent(this, StoreActivity::class.java)
            startActivity(intent)
        }

        friendIcon.setOnClickListener {
            val intent = Intent(this, FriendActivity::class.java)
            startActivity(intent)
        }

        infoIcon.setOnClickListener {
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
        }

        potatoIcon.setOnClickListener {
            val intent = Intent(this, PotatoActivity::class.java)
            startActivity(intent)
        }

    }
}