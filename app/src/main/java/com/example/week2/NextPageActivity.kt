package com.example.week2

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class NextPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.next_page)

        val mealIcon: ImageView = findViewById(R.id.meal_icon)

        mealIcon.setOnClickListener {
            val intent = Intent(this, MealActivity::class.java)
            startActivity(intent)
        }

    }
}