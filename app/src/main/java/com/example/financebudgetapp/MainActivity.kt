package com.example.financebudgetapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.financebudgetapp.ui.navigation.Home
import com.example.financebudgetapp.ui.database.*
import com.example.financebudgetapp.ui.theme.FinanceBudgetAppTheme
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = ExpenseDatabase.getDatabase(this)
        val dao = db.expenseDao()

        lifecycleScope.launch {
            //dao.insertExpense(ExpenseItem(name = "Coffee2", amount = 2.50)) //for testing

            dao.getAllExpensesFlow().collect { expenses ->
                // This block will be executed whenever the data in the database changes
                expenses.forEach {
                    println("Expense: ${it.name} - ${it.amount}")
                }
            }
        }

        setContent {
            Home()
        }
    }
}

