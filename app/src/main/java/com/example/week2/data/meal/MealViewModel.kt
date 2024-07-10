package com.example.week2

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MealViewModel(private val repository: MealRepository) : ViewModel() {

    val todayMeals: LiveData<List<Meal>> = repository.todayMeals.asLiveData()
    val todayMealCostSum: LiveData<Int> = repository.getTodayMealCostSum()
    val yesterdayMealCostSum: LiveData<Int> = repository.getYesterdayMealCostSum()

    fun insert(meal: Meal) = viewModelScope.launch {
        repository.insert(meal)
    }

    fun update(meal: Meal) = viewModelScope.launch {
        repository.update(meal)
    }

    fun delete(meal: Meal) = viewModelScope.launch {
        repository.delete(meal)
    }

    fun getMealsByDate(date: String): LiveData<List<Meal>> {
        return repository.getMealsByDate(date).asLiveData()
    }

}

class MealViewModelFactory(private val repository: MealRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MealViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}