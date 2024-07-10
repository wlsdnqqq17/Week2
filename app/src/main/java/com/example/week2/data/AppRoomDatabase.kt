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
            val meal = Meal( mealTime = "점심", mealName = "피자", price = 5000, date = "2024-07-10", memo = "")
            mealDao.insert(meal)
            val item1 = Item(
                id = 1,
                name = "naked",
                category = "clothes",
                item_image_url = "http://ec2-13-124-112-168.ap-northeast-2.compute.amazonaws.com/MyAvatar_item/Naked.png",
                price = 2,
                isPurchased = true)
            val item2 = Item(
                id = 3,
                name = "haksa",
                category = "hat",
                item_image_url = "http://ec2-13-124-112-168.ap-northeast-2.compute.amazonaws.com/MyAvatar_item/haksa.png",
                price = 1,
                isPurchased = true)
            val item3 = Item(
                id = 8,
                name = "butterfly",
                category = "accessory",
                item_image_url = "http://ec2-13-124-112-168.ap-northeast-2.compute.amazonaws.com/MyAvatar_item/butterfly.png",
                price = 2,
                isPurchased = true)
            itemDao.insert(item1)
            itemDao.insert(item2)
            itemDao.insert(item3)

        }
    }
}

