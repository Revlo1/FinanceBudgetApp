package com.example.financebudgetapp.ui.database

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow // Import Flow

class ExpenseRepository(private val expenseItemDao: ExpenseDao) {

    // Expose the data as a Flow from the DAO
    val allExpenses: Flow<List<ExpenseItem>> = expenseItemDao.getAllExpensesFlow() // Assuming you have getAllExpensesFlow() in your DAO

    @WorkerThread
    suspend fun insertExpense(expenseItem: ExpenseItem) {
        expenseItemDao.insertExpense(expenseItem)
    }

    @WorkerThread
    suspend fun updateExpense(expenseItem: ExpenseItem) {
        expenseItemDao.updateExpense(expenseItem)
    }

    @WorkerThread
    suspend fun deleteExpenseById(expenseId: Int) {
        expenseItemDao.deleteExpenseById(expenseId)
    }
}