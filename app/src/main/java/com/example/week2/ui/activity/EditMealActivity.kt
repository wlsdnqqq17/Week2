package com.example.week2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EditMealActivity : AppCompatActivity() {

    private lateinit var editMealTime: Spinner
    private lateinit var editMealName: EditText
    private lateinit var editPrice: EditText
    private lateinit var editDate: EditText
    private lateinit var editMemo: EditText

    private var mealId: Int = -1 // 추가

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_meal)

        editMealTime = findViewById(R.id.spinner_meal_time)
        editMealName = findViewById(R.id.edit_meal_name)
        editPrice = findViewById(R.id.edit_price)
        editDate = findViewById(R.id.edit_date)
        editMemo = findViewById(R.id.edit_memo)

        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate: String = LocalDate.now().format(formatter)
        editDate.setText(formattedDate)

        ArrayAdapter.createFromResource(
            this,
            R.array.meal_times_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            editMealTime.adapter = adapter
        }

        // 전달된 인텐트에서 데이터 설정
        intent?.let {
            mealId = it.getIntExtra(EXTRA_MEAL_ID, -1) // mealId 설정
            editMealTime.setSelection((editMealTime.adapter as ArrayAdapter<String>).getPosition(it.getStringExtra(EXTRA_MEAL_TIME)))
            editMealName.setText(it.getStringExtra(EXTRA_MEAL_NAME))
            editPrice.setText(it.getIntExtra(EXTRA_PRICE, 0).toString())
            editDate.setText(it.getStringExtra(EXTRA_DATE))
            editMemo.setText(it.getStringExtra(EXTRA_MEMO))
        }

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editMealTime.selectedItem.toString()) ||
                TextUtils.isEmpty(editMealName.text) ||
                TextUtils.isEmpty(editPrice.text) ||
                TextUtils.isEmpty(editDate.text)
            ) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val mealTime = editMealTime.selectedItem.toString()
                val mealName = editMealName.text.toString()
                val price = editPrice.text.toString().toIntOrNull() ?: 0
                val date = editDate.text.toString()
                val memo = editMemo.text.toString()

                replyIntent.putExtra(EXTRA_MEAL_ID, mealId) // mealId 추가
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
        const val EXTRA_MEAL_ID = "com.example.week2.MEAL_ID"
        const val EXTRA_MEAL_TIME = "com.example.week2.MEAL_TIME"
        const val EXTRA_MEAL_NAME = "com.example.week2.MEAL_NAME"
        const val EXTRA_PRICE = "com.example.week2.PRICE"
        const val EXTRA_DATE = "com.example.week2.DATE"
        const val EXTRA_MEMO = "com.example.week2.MEMO"
    }
}
