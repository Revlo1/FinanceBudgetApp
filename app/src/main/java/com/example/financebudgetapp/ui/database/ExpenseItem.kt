package com.example.financebudgetapp.ui.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "expenses")
data class ExpenseItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val amount: Double,
    val date: Date = Date()
)
