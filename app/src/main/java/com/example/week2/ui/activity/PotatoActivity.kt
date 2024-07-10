package com.example.week2

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.week2.data.meal.WordsApplication
import java.text.SimpleDateFormat
import java.util.*

class PotatoActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var potatoCount: TextView
    private lateinit var todayButton: Button
    private lateinit var missionButton: Button
    private lateinit var developerButton: Button

    private val mealViewModel: MealViewModel by viewModels {
        MealViewModelFactory((application as WordsApplication).mealRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_potato)

        sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        potatoCount = findViewById(R.id.potato_count)
        todayButton = findViewById(R.id.today_potato)
        missionButton = findViewById(R.id.mission_potato)
        developerButton = findViewById(R.id.developer_potato)

        val toolbar: Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        updatePotatoCount()
        checkButtonAvailability(todayButton, "TodayLastClickDate", "오늘의 감자 받기", "오늘의 감자 받음")
        checkMissionButtonAvailability()

        todayButton.setOnClickListener {
            incrementPotatoCount("TodayLastClickDate")
            updateButtonState(todayButton, false, "오늘의 감자 받음")
        }

        missionButton.setOnClickListener {
            incrementPotatoCount("MissionLastClickDate")
            updateButtonState(missionButton, false, "미션 감자 받음")
        }

        developerButton.setOnClickListener {
            incrementPotatoCount()
        }
    }

    private fun updatePotatoCount() {
        val savedInt = sharedPreferences.getInt("Potato", 0)
        potatoCount.text = "$savedInt"
    }

    private fun checkButtonAvailability(button: Button, dateKey: String, enabledText: String, disabledText: String) {
        val lastClickDate = sharedPreferences.getString(dateKey, "")
        val currentDate = getCurrentDate()
        if (lastClickDate == currentDate) {
            updateButtonState(button, false, disabledText)
        } else {
            updateButtonState(button, true, enabledText)
        }
    }

    private fun checkMissionButtonAvailability() {
        val lastClickDate = sharedPreferences.getString("MissionLastClickDate", "")
        val currentDate = getCurrentDate()

        if (lastClickDate == currentDate) {
            updateButtonState(missionButton, false, "미션 감자 받음")
        } else {
            mealViewModel.yesterdayMealCostSum.observe(this) { costSum ->
                val budget = sharedPreferences.getInt("Budget", 0)
                if (costSum < budget) {
                    updateButtonState(missionButton, true, "미션 감자 받기")
                } else {
                    updateButtonState(missionButton, false, "미션 실패!")
                }
            }
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun updateButtonState(button: Button, isEnabled: Boolean, text: String) {
        button.isEnabled = isEnabled
        button.setBackgroundResource(if (isEnabled) R.drawable.button_background else R.drawable.button_background_disabled)
        button.text = text
    }

    private fun incrementPotatoCount(dateKey: String? = null) {
        val currentCount = sharedPreferences.getInt("Potato", 0)
        val newCount = currentCount + 1
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt("Potato", newCount)
        dateKey?.let {
            editor.putString(it, getCurrentDate())
        }
        editor.apply()
        potatoCount.text = "$newCount"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
