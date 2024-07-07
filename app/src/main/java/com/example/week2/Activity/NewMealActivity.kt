package com.example.week2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class NewMealActivity : AppCompatActivity() {

    private lateinit var editMealTime: EditText
    private lateinit var editMealName: EditText
    private lateinit var editPrice: EditText
    private lateinit var editDate: EditText
    private lateinit var editMemo: EditText

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_meal)

        editMealTime = findViewById(R.id.edit_meal_time)
        editMealName = findViewById(R.id.edit_meal_name)
        editPrice = findViewById(R.id.edit_price)
        editDate = findViewById(R.id.edit_date)
        editMemo = findViewById(R.id.edit_memo)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editMealTime.text) || TextUtils.isEmpty(editMealName.text) || TextUtils.isEmpty(editPrice.text) || TextUtils.isEmpty(editDate.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val mealTime = editMealTime.text.toString()
                val mealName = editMealName.text.toString()
                val price = editPrice.text.toString().toInt()
                val date = editDate.text.toString()
                val memo = editMemo.text.toString()

                replyIntent.putExtra(EXTRA_MEAL_TIME, mealTime)
                replyIntent.putExtra(EXTRA_MEAL_NAME, mealName)
                replyIntent.putExtra(EXTRA_PRICE, price)
                replyIntent.putExtra(EXTRA_DATE, date)
                replyIntent.putExtra(EXTRA_MEMO, memo)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_MEAL_TIME = "com.example.week2.MEAL_TIME"
        const val EXTRA_MEAL_NAME = "com.example.week2.MEAL_NAME"
        const val EXTRA_PRICE = "com.example.week2.PRICE"
        const val EXTRA_DATE = "com.example.week2.DATE"
        const val EXTRA_MEMO = "com.example.week2.MEMO"
    }
}