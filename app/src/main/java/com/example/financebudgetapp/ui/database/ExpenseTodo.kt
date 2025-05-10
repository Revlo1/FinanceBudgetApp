package com.example.financebudgetapp.ui.database

import android.app.Application


class ExpenseTodo: Application() {
    private val database by lazy {ExpenseDatabase.getDatabase(this)}
    val repository by lazy {ExpenseRepository(database.expenseDao())}

}
