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
import java.util.Date // Import Date
import java.util.Locale
import java.util.Calendar // Import Calendar

@OptIn(ExperimentalMaterial3Api::class)
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

    // Group expenses by day
    val groupedExpenses = remember(expenses) {
        expenses.groupBy { expense ->
            // Get the date part of the expense date
            val calendar = Calendar.getInstance()
            calendar.time = expense.date
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            calendar.time // Use the date without time for grouping
        }
    }

    // Format for displaying the day header (Day, Month in text, Year)
    val dayFormat = remember { SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()) } // Changed pattern

    Scaffold(
        // topBar is removed
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

                    // Removed the text label for date format

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Iterate through the grouped expenses (Map<Date, List<ExpenseItem>>)
                        groupedExpenses.forEach { (date, expensesForDay) ->
                            // Add a header for the day
                            item {
                                Text(
                                    text = dayFormat.format(date), // Format and display the date
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }

                            // Add the expense items for this day
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

                // Explicitly place the FloatingActionButton at the bottom end
                if (selectedExpenseIds.isNotEmpty()) {
                    FloatingActionButton(
                        onClick = {
                            selectedExpenseIds.forEach { id ->
                                expenseViewModel.deleteExpenseById(id)
                            }
                            selectedExpenseIds = emptySet() // Clear selection after deletion
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd) // Align to the bottom end of the Box
                            .padding(16.dp) // Add some padding
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

    // Date format for displaying only the time
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) } // Changed pattern to "HH:mm"
    val formattedTime = timeFormat.format(expense.date) // Format the date to time

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
                Text(text = "Time: $formattedTime") // Display the formatted time
            }
        }
    }
}