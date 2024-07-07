package com.example.week2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MealActivity : AppCompatActivity() {

    private val mealViewModel: MealViewModel by viewModels {
        MealViewModelFactory((application as WordsApplication).mealRepository)
    }

    private lateinit var newMealActivityLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = MealListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MealActivity, NewMealActivity::class.java)
            newMealActivityLauncher.launch(intent)
        }

        newMealActivityLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    val mealTime = data.getStringExtra(NewMealActivity.EXTRA_MEAL_TIME) ?: ""
                    val mealName = data.getStringExtra(NewMealActivity.EXTRA_MEAL_NAME) ?: ""
                    val price = data.getIntExtra(NewMealActivity.EXTRA_PRICE, 0)
                    val date = data.getStringExtra(NewMealActivity.EXTRA_DATE) ?: ""
                    val memo = data.getStringExtra(NewMealActivity.EXTRA_MEMO)

                    val meal = Meal(
                        mealTime = mealTime,
                        mealName = mealName,
                        price = price,
                        date = date,
                        memo = memo
                    )
                    mealViewModel.insert(meal)
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        mealViewModel.todayMeals.observe(this) { meals ->
            meals.let { adapter.submitList(it) }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}