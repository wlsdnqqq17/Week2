package com.example.week2.data


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.week2.Meal
import com.example.week2.MealDao
import com.example.week2.data.item.Item
import com.example.week2.data.item.ItemDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Meal::class, Item::class], version = 1)
abstract class AppRoomDatabase : RoomDatabase() {

    //abstract fun wordDao(): WordDao
    abstract fun mealDao(): MealDao
    abstract fun itemDao(): ItemDao

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
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.mealDao(), database.itemDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(mealDao: MealDao, itemDao: ItemDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
//            wordDao.deleteAll()
//
//            var word = Word("Hello")
//            wordDao.insert(word)
//            word = Word("World!")
//            wordDao.insert(word)

            var meal = Meal( mealTime = "점심", mealName = "피자", price = 5000, date = "2024-07-08", memo = "")
            mealDao.insert(meal)
            var item1 = Item( name = "벗기", category = "옷", imageUri = "", price = 3)
            itemDao.insert(item1)
            var item2 = Item( name = "거위", category = "배경", imageUri = "", price = 2)
            itemDao.insert(item2)
        }
    }
}

