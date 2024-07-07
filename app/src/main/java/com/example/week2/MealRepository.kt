package com.example.week2

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MealRepository(private val mealDao: MealDao) {

    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    val todayMeals: Flow<List<Meal>>
        get() {
            val today: LocalDate = LocalDate.now()
            val formattedDate: String = today.format(formatter)
            return mealDao.getMealsByDate(formattedDate)
        }

    @WorkerThread
    suspend fun insert(meal: Meal) {
        mealDao.insert(meal)
    }
}