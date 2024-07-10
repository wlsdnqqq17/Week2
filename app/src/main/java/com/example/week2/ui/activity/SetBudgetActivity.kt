package com.example.week2

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class SetBudgetActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_budget)

        val setBudgetView = findViewById<EditText>(R.id.set_budget)
        val button = findViewById<Button>(R.id.button_save)

        button.setOnClickListener {
            val inputValue = setBudgetView.text.toString().toIntOrNull()

            if (inputValue != null) {
                val sharedPreferences: SharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)

                val editor: SharedPreferences.Editor = sharedPreferences.edit()

                editor.putInt("Budget", inputValue)
                editor.apply()

                // 다음 Activity로 이동합니다.
                val intent = Intent(this, HomePageActivity::class.java)
                startActivity(intent)
            } else {
                setBudgetView.error = "Please enter a valid number"
            }


        }
    }
}
