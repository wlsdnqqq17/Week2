package com.example.week2

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MealRepository(private val mealDao: MealDao) {

    val todayMeals: Flow<List<Meal>>
        get() {
            return mealDao.getMealsByDate(getTodayDate())
        }

    fun getTodayMealCostSum(): LiveData<Int> {
        val today = getTodayDate()
        return mealDao.getTodayMealCostSum(today).asLiveData()
    }

    @WorkerThread
    suspend fun insert(meal: Meal) {
        mealDao.insert(meal)
    }

    @WorkerThread
    suspend fun update(meal: Meal) {
        mealDao.update(meal)
    }

    @WorkerThread
    suspend fun delete(meal: Meal) {
        mealDao.delete(meal)
    }

    private fun getTodayDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}
