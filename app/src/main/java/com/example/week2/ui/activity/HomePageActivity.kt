package com.example.week2

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class HomePageActivity : AppCompatActivity() {
    private var backPressedOnce = false
    private val handler = Handler(Looper.getMainLooper())
    private val backPressedRunnable = Runnable { backPressedOnce = false }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)
        val setBudgetButton: TextView = findViewById(R.id.budget_text)
        val mealIcon: ImageView = findViewById(R.id.meal_icon)
        val inventoryIcon: ImageView = findViewById(R.id.inventory)
        val calendarIcon: ImageView = findViewById(R.id.calendar_icon)
        val storeIcon: ImageView = findViewById(R.id.store_icon)
        val friendIcon: ImageView = findViewById(R.id.friend_icon)
        val potatoIcon: ImageView = findViewById(R.id.potato_icon)
        val infoIcon: ImageView = findViewById(R.id.info)
        val sharedPreferences: SharedPreferences = getSharedPreferences("Budget", MODE_PRIVATE)
        val savedInt = sharedPreferences.getInt("Budget", 0)

        setBudgetButton.text = "0 / $savedInt"

        setBudgetButton.setOnClickListener {
            val intent = Intent(this, SetBudgetActivity::class.java)
            startActivity(intent)
        }
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
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedOnce) {
                    finishAffinity()
                } else {
                    backPressedOnce = true
                    Toast.makeText(this@HomePageActivity, "한 번 더 누르면 앱이 종료됩니다", Toast.LENGTH_SHORT).show()
                    handler.postDelayed(backPressedRunnable, 2000)
                }
            }
        })

    }
}