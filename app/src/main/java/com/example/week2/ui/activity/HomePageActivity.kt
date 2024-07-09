package com.example.week2

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.week2.data.meal.WordsApplication

class HomePageActivity : AppCompatActivity() {
    private var backPressedOnce = false
    private val handler = Handler(Looper.getMainLooper())
    private val backPressedRunnable = Runnable { backPressedOnce = false }

    private val mealViewModel: MealViewModel by viewModels {
        MealViewModelFactory((application as WordsApplication).mealRepository)
    }

    @SuppressLint("SetTextI18n")
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
        val progressBar: ProgressBar = findViewById(R.id.progress_bar)
        val sharedPreferences: SharedPreferences = getSharedPreferences("Budget", MODE_PRIVATE)
        val savedInt = sharedPreferences.getInt("Budget", 0)

        mealViewModel.todayMealCostSum.observe(this) { costSum ->
            if (savedInt != 0) {
                setBudgetButton.text = "$costSum / $savedInt"
                val progress = ((costSum.toDouble() / savedInt) * 100).toInt()
                progressBar.progress = progress

                if (costSum > savedInt) {
                    setBudgetButton.text = "예산 초과!"
                    progressBar.progress = 100
                    setBudgetButton.setTextColor(Color.RED)
                    progressBar.progressDrawable = ContextCompat.getDrawable(this, R.drawable.progress_bar_over)
                } else {
                    setBudgetButton.setTextColor(Color.BLACK)
                    progressBar.progressDrawable = ContextCompat.getDrawable(this, R.drawable.progress_bar_normal)
                }
            }
        }


        setBudgetButton.setOnClickListener {
            val intent = Intent(this, SetBudgetActivity::class.java)
            startActivity(intent)
        }
        mealIcon.setOnClickListener {
            val intent = Intent(this, MealActivity::class.java)
            startActivity(intent)
        }

        inventoryIcon.setOnClickListener {
            val intent = Intent(this, BagActivity::class.java)
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