package com.example.week2.data


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.week2.Meal
import com.example.week2.MealDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Meal::class], version = 1)
abstract class AppRoomDatabase : RoomDatabase() {

    //abstract fun wordDao(): WordDao
    abstract fun mealDao(): MealDao

    companion object {
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): AppRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                //개발용 임~~~~
                context.deleteDatabase("app_database")
                //개발용 임~~~~
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class WordDatabaseCallback(
            private val scope: CoroutineScope
        ) : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.mealDao())
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
        suspend fun populateDatabase(mealDao: MealDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
//            wordDao.deleteAll()
//
//            var word = Word("Hello")
//            wordDao.insert(word)
//            word = Word("World!")
//            wordDao.insert(word)

            var meal = Meal( mealTime = "ㅇ", mealName = "치킨", price = 5000, date = "2024-07-08", memo = "")
            mealDao.insert(meal)
        }
    }
}

