
package com.example.financebudgetapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.financebudgetapp.api.RetrofitClient
import com.example.financebudgetapp.api.CurrencyRepository
import com.example.financebudgetapp.api.CurrencyViewModel
import com.example.financebudgetapp.api.CurrencyViewModelFactory
import com.example.financebudgetapp.api.Result
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyDisplay(
    modifier: Modifier = Modifier,
    currencyViewModel: CurrencyViewModel = viewModel(
        factory = CurrencyViewModelFactory(CurrencyRepository(RetrofitClient.currencyApiService))
    ),

    amount: Double,
    label: String
) {
    val selectedBaseCurrency by currencyViewModel.selectedBaseCurrency.collectAsState()
    val availableCurrencies by currencyViewModel.availableCurrencies.collectAsState()
    val exchangeRatesResult by currencyViewModel.exchangeRates.collectAsState()

    val decimalFormat = remember { DecimalFormat("0.00") }

    fun convertToSelectedCurrency(amount: Double): Double {
        return when (exchangeRatesResult) {
            is Result.Success -> {
                val exchangeRates = (exchangeRatesResult as Result.Success).data.conversionRates
                val rateFromGBP = exchangeRates["GBP"]
                val rateToSelected = exchangeRates[selectedBaseCurrency]

                if (rateFromGBP != null && rateToSelected != null && rateFromGBP != 0.0) {
                    (amount / rateFromGBP) * rateToSelected
                } else if (selectedBaseCurrency == "GBP") {
                    amount
                } else {
                    amount
                }
            }
            else -> amount
        }
    }

    val convertedAmount = convertToSelectedCurrency(amount)
    val formattedConvertedAmount = decimalFormat.format(convertedAmount)

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, fontWeight = FontWeight.Bold)

            //currency selection dropdown box
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    modifier = Modifier
                        .menuAnchor()
                        .width(120.dp),
                    readOnly = true,
                    value = selectedBaseCurrency,
                    onValueChange = {},
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurface
                    ),
                    textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    availableCurrencies.forEach { currencyCode ->
                        DropdownMenuItem(
                            text = { Text(currencyCode) },
                            onClick = {
                                currencyViewModel.selectBaseCurrency(currencyCode)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
        Text(text = "$selectedBaseCurrency$formattedConvertedAmount", fontSize = 24.sp)
    }
}