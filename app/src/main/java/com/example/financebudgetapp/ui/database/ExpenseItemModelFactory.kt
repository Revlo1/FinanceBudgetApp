package com.example.financebudgetapp.ui.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class ExpenseItemModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {

            val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as ExpenseTodo

            val repository = application.repository

            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(repository) as T
        }
        throw IllegalArgumentException("Error: Unknown Class")
    }
}