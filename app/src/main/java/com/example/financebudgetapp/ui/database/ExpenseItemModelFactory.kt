package com.example.financebudgetapp.ui.database

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

// This factory will provide the ExpenseRepository to the ExpenseViewModel
class ExpenseItemModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(ExpenseViewModel::class.java)) {
            // Get the Application instance from CreationExtras
            val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as ExpenseTodo

            // Access the repository directly from your custom Application class
            val repository = application.repository

            @Suppress("UNCHECKED_CAST")
            return ExpenseViewModel(repository) as T
        }
        throw IllegalArgumentException("Error: Unknown Class")
    }
}