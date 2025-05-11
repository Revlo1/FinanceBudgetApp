package com.example.financebudgetapp.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.financebudgetapp.ui.database.ExpenseItem
import com.example.financebudgetapp.ui.database.ExpenseViewModel
import com.example.financebudgetapp.ui.database.ExpenseItemModelFactory
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Calendar

@Composable
fun ShowExpenseScreen(
    modifier: Modifier = Modifier,
    expenseViewModel: ExpenseViewModel = viewModel(factory = ExpenseItemModelFactory())
) {
    val expenses by expenseViewModel.allExpenses.collectAsState()
    val totalSpent by expenseViewModel.totalSpent.collectAsState()
    var selectedExpenseIds by remember { mutableStateOf(emptySet<Int>()) }

    val decimalFormat = remember { DecimalFormat("0.00") }
    val formattedTotal = decimalFormat.format(totalSpent)

    val groupedExpenses = remember(expenses) {
        expenses.groupBy { expense ->
            val calendar = Calendar.getInstance()
            calendar.time = expense.date
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            calendar.time
        }
    }

    val dayFormat = remember { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()) }

    Scaffold(
        content = { paddingValues ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color(0xFF77C770))
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Total Spent: £$formattedTotal",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )


                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        groupedExpenses.forEach { (date, expensesForDay) ->
                            item {
                                Text(
                                    text = dayFormat.format(date),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }

                            items(
                                items = expensesForDay,
                                key = { expense -> expense.id }
                            ) { expense ->
                                val isSelected = selectedExpenseIds.contains(expense.id)
                                ExpenseItemComposable(
                                    expense = expense,
                                    isSelected = isSelected,
                                    onSelectClick = {
                                        selectedExpenseIds = if (isSelected) {
                                            selectedExpenseIds - expense.id
                                        } else {
                                            selectedExpenseIds + expense.id
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                if (selectedExpenseIds.isNotEmpty()) {
                    FloatingActionButton(
                        onClick = {
                            selectedExpenseIds.forEach { id ->
                                expenseViewModel.deleteExpenseById(id)
                            }
                            selectedExpenseIds = emptySet()
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete selected")
                    }
                }
            }
        }
    )
}

@Composable
fun ExpenseItemComposable(
    expense: ExpenseItem,
    isSelected: Boolean,
    onSelectClick: () -> Unit
) {
    val decimalFormat = remember { DecimalFormat("0.00") }
    val formattedAmount = decimalFormat.format(expense.amount)

    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val formattedTime = timeFormat.format(expense.date)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelectClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFB2DFDB) else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Name: ${expense.name}", fontWeight = FontWeight.Bold)
                Text(text = "Amount: £$formattedAmount")
                Text(text = "Time: $formattedTime")
            }
        }
    }
}