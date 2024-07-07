package com.example.week2

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MealsApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { AppRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { MealRepository(database.mealDao()) }
}