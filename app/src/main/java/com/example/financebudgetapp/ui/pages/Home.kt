package com.example.financebudgetapp.ui.pages

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.financebudgetapp.R
import com.example.financebudgetapp.api.RetrofitClient // Import RetrofitClient
import com.example.financebudgetapp.api.CurrencyRepository // Import CurrencyRepository
import com.example.financebudgetapp.ui.CurrencyDisplay // Import CurrencyDisplay
import com.example.financebudgetapp.api.CurrencyViewModel // Import CurrencyViewModel
import com.example.financebudgetapp.api.CurrencyViewModelFactory // Import CurrencyViewModelFactory
import com.example.financebudgetapp.ui.database.ExpenseViewModel
import com.example.financebudgetapp.ui.database.BudgetViewModel
import com.example.financebudgetapp.ui.database.ExpenseItemModelFactory
import com.example.financebudgetapp.widget.BudgetWidgetProvider
import java.text.DecimalFormat
import kotlin.text.format

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    expenseViewModel: ExpenseViewModel = viewModel(factory = ExpenseItemModelFactory()),
    budgetViewModel: BudgetViewModel = viewModel(),
    // Get the CurrencyViewModel here as well
    currencyViewModel: CurrencyViewModel = viewModel(
        factory = CurrencyViewModelFactory(CurrencyRepository(RetrofitClient.currencyApiService))
    )
) {
    val totalSpent by expenseViewModel.totalSpent.collectAsState()
    val budget by budgetViewModel.budget.collectAsState()
    var showBudgetDialog by remember { mutableStateOf(false) }
    var budgetInput by remember { mutableStateOf("") }

    // State to control the visibility of the CurrencyDisplay
    var showCurrencyDisplay by remember { mutableStateOf(false) }

    val decimalFormat = remember { DecimalFormat("0.00") }
    val formattedTotal = decimalFormat.format(totalSpent)
    val formattedBudget = decimalFormat.format(budget)
    val remaining = budget - totalSpent
    val formattedRemaining = decimalFormat.format(remaining)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF3995B4))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Finance Overview",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Total Spent:", fontWeight = FontWeight.Bold)
                Text(text = "£$formattedTotal", fontSize = 24.sp)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Budget:", fontWeight = FontWeight.Bold)
                Text(text = "£$formattedBudget", fontSize = 24.sp)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = when {
                    remaining >= 0 -> Color(0xFFC8E6C9)
                    else -> Color(0xFFFFCDD2)
                }
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = if (remaining >= 0) "Remaining:" else "Over Budget:", fontWeight = FontWeight.Bold)
                Text(
                    text = "£$formattedRemaining",
                    fontSize = 24.sp,
                    color = when {
                        remaining >= 0 -> Color.Black
                        else -> Color.Red
                    }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Button to toggle CurrencyDisplay visibility
        Button(onClick = { showCurrencyDisplay = !showCurrencyDisplay }) {
            Text(if (showCurrencyDisplay) "Hide Currency Converter" else "Show Currency Converter")
        }

        // Conditionally display the CurrencyDisplay Composable
        if (showCurrencyDisplay) {
            // You can choose which value to display in the CurrencyDisplay
            // For example, display the Total Spent in the selected currency
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    CurrencyDisplay(
                        currencyViewModel = currencyViewModel,
                        amount = totalSpent, // Pass the amount you want to convert
                        label = "Total Spent (Converted):" // Label for the converted amount
                    )
                }
            }
            // You could add more CurrencyDisplay instances for Budget or Remaining if needed
            // Card(...) { Column(...) { CurrencyDisplay(...) } }
        }


        Button(onClick = {
            showBudgetDialog = true
            budgetInput = budget.toString()
        }) {
            Text("Set/Edit Budget")
        }
    }
    val context = LocalContext.current // Get context here (e.g., from a LocalContext.current)
    // Budget Setting Dialog
    if (showBudgetDialog) {
        AlertDialog(
            onDismissRequest = { showBudgetDialog = false },
            title = { Text("Set Budget") },
            text = {
                OutlinedTextField(
                    value = budgetInput,
                    onValueChange = { budgetInput = it },
                    label = { Text("Budget Amount") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )
            },
            confirmButton = {
                Button(onClick = {
                    val newBudget = budgetInput.toDoubleOrNull() ?: 0.0
                    budgetViewModel.updateBudget(newBudget)
                    showBudgetDialog = false

                    // Save the new budget to SharedPreferences

                    val sharedPreferences = context.getSharedPreferences("FinancePrefs", Context.MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putFloat("budget", newBudget.toFloat()) // Save the budget as a Float
                        putFloat("total_spent", totalSpent.toFloat())
                        apply() // Use apply() for asynchronous saving
                    }

                    // Trigger widget update after saving
                    val appWidgetManager = AppWidgetManager.getInstance(context)
                    val componentName = ComponentName(context, BudgetWidgetProvider::class.java)
                    val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.budget_overview_text)

                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = { showBudgetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}