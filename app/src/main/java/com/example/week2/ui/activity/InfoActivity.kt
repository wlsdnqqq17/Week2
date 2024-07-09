package com.example.week2

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.textservice.TextInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val IdTextView: TextView = findViewById(R.id.info_id)
        val nameTextView: TextView = findViewById(R.id.info_name)

        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val myLoginId = sharedPref.getString("login_id", null)
        if (myLoginId != null) {
            IdTextView.text = myLoginId
        } else {
            Log.d("myLoginId", "login 아이디가 없음")
        }
        val myName = sharedPref.getString("nickname", null)
        if (myName != null) {
            nameTextView.text = myName
        } else {
            Log.d("myName", "nickname이 없음")
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
