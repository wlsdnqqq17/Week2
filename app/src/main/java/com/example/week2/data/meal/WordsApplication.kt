package com.example.week2.data.meal

import android.app.Application
import com.example.week2.data.AppRoomDatabase
import com.example.week2.MealRepository
import com.example.week2.R
import com.example.week2.data.item.ItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import com.kakao.sdk.common.KakaoSdk

class WordsApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { AppRoomDatabase.getDatabase(this, applicationScope) }
    //val wordRepository by lazy { WordRepository(database.wordDao()) }
    val mealRepository by lazy { MealRepository(database.mealDao()) }
    val itemRepository by lazy { ItemRepository(database.itemDao()) }

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, getString(R.string.kakao_native_key))
    }
}