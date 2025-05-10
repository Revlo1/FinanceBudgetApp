package com.example.financebudgetapp.ui.database // You can put this in a different package if you prefer, e.g., ui.budget

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BudgetViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("budget_prefs", Application.MODE_PRIVATE)
    private val BUDGET_KEY = "current_budget"

    private val _budget = MutableStateFlow(0.0) // Default budget
    val budget: StateFlow<Double> = _budget

    init {
        // Load budget from SharedPreferences when the ViewModel is created
        loadBudget()
    }

    fun updateBudget(newBudget: Double) {
        viewModelScope.launch {
            _budget.value = newBudget
            saveBudget(newBudget)
        }
    }

    private fun loadBudget() {
        val savedBudget = prefs.getFloat(BUDGET_KEY, 0.0f).toDouble() // Default to 0.0 if not found
        _budget.value = savedBudget
    }

    private fun saveBudget(budget: Double) {
        with(prefs.edit()) {
            putFloat(BUDGET_KEY, budget.toFloat())
            apply() // Use apply() for asynchronous saving
        }
    }
}