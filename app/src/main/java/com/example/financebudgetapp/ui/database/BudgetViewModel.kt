package com.example.financebudgetapp.ui.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BudgetViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("budget_prefs", Application.MODE_PRIVATE)
    private val BUDGET_KEY = "current_budget"

    private val _budget = MutableStateFlow(0.0)
    val budget: StateFlow<Double> = _budget

    init {
        loadBudget()
    }

    fun updateBudget(newBudget: Double) {
        viewModelScope.launch {
            _budget.value = newBudget
            saveBudget(newBudget)
        }
    }

    private fun loadBudget() {
        val savedBudget = prefs.getFloat(BUDGET_KEY, 0.0f).toDouble()
        _budget.value = savedBudget
    }

    private fun saveBudget(budget: Double) {
        with(prefs.edit()) {
            putFloat(BUDGET_KEY, budget.toFloat())
            apply()
        }
    }
}