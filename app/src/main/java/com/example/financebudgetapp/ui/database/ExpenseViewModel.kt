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
            .map { expenses -> expenses.sumOf { it.amount } } // Sum the amount of each expense
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = 0.0 // Initial total is 0.0
            )

    fun addExpenseItem(name: String, amount: Double) = viewModelScope.launch {
        val expense = ExpenseItem(name = name, amount = amount)
        repository.insertExpense(expense)
    }

    fun updateExpenseItem(expense: ExpenseItem) = viewModelScope.launch {
        repository.updateExpense(expense)
    }

    fun deleteExpenseById(expenseId: Int) = viewModelScope.launch {
        repository.deleteExpenseById(expenseId)
    }

}
/*
class ExpenseItemModelFactory(private val repository: ExpenseRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Log.d("Factory", "Creating ExpenseViewModel")
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java))
            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(repository) as T
        throw IllegalArgumentException("Error: Unknown Class")
    }
}

*/