package com.example.financebudgetapp.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel // Import viewModel
import com.example.financebudgetapp.ui.database.ExpenseViewModel
import com.example.financebudgetapp.ui.database.ExpenseItemModelFactory // Import your factory

@Composable
fun AddExpenseScreen(
    modifier: Modifier = Modifier,
    expenseViewModel: ExpenseViewModel = viewModel(factory = ExpenseItemModelFactory())
) {
    var expenseName by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFA870B9))
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add An Expense",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = expenseName,
            onValueChange = { expenseName = it },
            label = { Text("Expense Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = expenseAmount,
            onValueChange = { expenseAmount = it },
            label = { Text("Amount (e.g 3.75)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (expenseName.isNotEmpty() && expenseAmount.isNotEmpty()) {
                    val amountDouble = expenseAmount.toDoubleOrNull()
                    if (amountDouble != null) {

                        expenseViewModel.addExpenseItem(expenseName, amountDouble)

                        expenseName = ""
                        expenseAmount = ""
                    }
                }
            }
        ) {
            Text("Save Expense")
        }
    }
}