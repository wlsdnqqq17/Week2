package com.example.week2

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class PotatoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_potato)

        val sharedPreferences: SharedPreferences = getSharedPreferences("Potato", MODE_PRIVATE)
        val potatoCount: TextView = findViewById(R.id.potato_count)
        val savedInt = sharedPreferences.getInt("Potato", 0)

        potatoCount.text = "$savedInt"

        val button = findViewById<Button>(R.id.receive_button)
        button.setOnClickListener {
            val currentCount = sharedPreferences.getInt("Potato", 0)
            val newCount = currentCount + 1
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putInt("Potato", newCount)
            editor.apply()
            potatoCount.text = "$newCount"
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
