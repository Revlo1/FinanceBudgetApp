package com.example.financebudgetapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.financebudgetapp.ui.database.ExpenseDatabase
import com.example.financebudgetapp.ui.database.ExpenseItemModelFactory
import com.example.financebudgetapp.ui.database.ExpenseRepository
import com.example.financebudgetapp.ui.database.ExpenseViewModel
import com.example.financebudgetapp.ui.pages.AddExpenseScreen
import com.example.financebudgetapp.ui.pages.HomeScreen
import com.example.financebudgetapp.ui.pages.ShowExpenseScreen


@Composable
fun Home(modifier: Modifier = Modifier) {
    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home),
        NavItem("AddExpense", Icons.Default.Add),
        NavItem("ShowExpense", Icons.AutoMirrored.Filled.List)
    )

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    /*
    val context = LocalContext.current
    val database = ExpenseDatabase.getDatabase(context)
    val repository = ExpenseRepository(database.expenseDao())
    val expenseViewModel: ExpenseViewModel = viewModel(
        factory = ExpenseItemModelFactory(repository)
    )

    */

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {selectedIndex = index},
                        icon = {
                            Icon(imageVector = navItem.icon, contentDescription = "Icon")
                        },
                        label = {Text(text = navItem.label)}
                    )
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(modifier = Modifier.padding(innerPadding), selectedIndex = selectedIndex)
    }
}


@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex: Int) {
    when (selectedIndex) {
        0 -> {
            HomeScreen(modifier)
        }
        1 -> {
            AddExpenseScreen(modifier)
        }
        2 -> {
            ShowExpenseScreen(modifier)
        }
    }
}