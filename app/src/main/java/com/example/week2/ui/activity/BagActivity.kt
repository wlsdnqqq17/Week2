package com.example.week2

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class BagActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bag)

        val char_button: Button = findViewById(R.id.char_button)
        val hat_button: Button = findViewById(R.id.hat_button)
        val acc_button: Button = findViewById(R.id.acc_button)
        val bg_button: Button = findViewById(R.id.bg_button)

        char_button.setOnClickListener {
            val intent = Intent(this, ItemCharActivity::class.java)
            startActivity(intent)
        }
        hat_button.setOnClickListener {
            val intent = Intent(this, ItemHatActivity::class.java)
            startActivity(intent)
        }
        acc_button.setOnClickListener {
            val intent = Intent(this, ItemAccActivity::class.java)
            startActivity(intent)
        }
        bg_button.setOnClickListener {
            val intent = Intent(this, ItemBgActivity::class.java)
            startActivity(intent)
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}

