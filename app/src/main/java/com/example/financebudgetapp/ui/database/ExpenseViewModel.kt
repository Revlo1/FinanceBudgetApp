package com.example.financebudgetapp.ui.database

import androidx.lifecycle.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class ExpenseViewModel(private val repository: ExpenseRepository): ViewModel() {
    val allExpenses: StateFlow<List<ExpenseItem>> =
        repository.allExpenses
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val totalSpent: StateFlow<Double> =
        allExpenses
            .map { expenses -> expenses.sumOf { it.amount } }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0.0
            )

    fun addExpenseItem(name: String, amount: Double) = viewModelScope.launch {
        val expense = ExpenseItem(name = name, amount = amount)
        repository.insertExpense(expense)
    }

    /* //for adding an update expense feature
    fun updateExpenseItem(expense: ExpenseItem) = viewModelScope.launch {
        repository.updateExpense(expense)
    }
    */

    fun deleteExpenseById(expenseId: Int) = viewModelScope.launch {
        repository.deleteExpenseById(expenseId)
    }

}