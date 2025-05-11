package com.example.financebudgetapp.ui.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ExpenseItem::class], version = 2, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        fun getDatabase(context: Context): ExpenseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    "expense_database"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}