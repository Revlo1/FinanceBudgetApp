package com.example.financebudgetapp.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.financebudgetapp.api.ExchangeRateResponse
import com.example.financebudgetapp.api.CurrencyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CurrencyViewModel(private val repository: CurrencyRepository) : ViewModel() {

    private val _exchangeRates = MutableStateFlow<Result<ExchangeRateResponse>>(Result.Loading)
    val exchangeRates: StateFlow<Result<ExchangeRateResponse>> = _exchangeRates

    private val _selectedBaseCurrency = MutableStateFlow("GBP") // Default base currency
    val selectedBaseCurrency: StateFlow<String> = _selectedBaseCurrency

    // **Replace with your actual ExchangeRate-API key**
    private val API_KEY = "c25e5dc8fc7e760e3eb59da5"

    // You'll likely want a list of currencies for the dropdown.
    // For simplicity, we'll hardcode a few here. In a real app, you'd fetch this from the API
    // or a local source.
    private val _availableCurrencies = MutableStateFlow(listOf("USD", "EUR", "GBP", "JPY", "CAD", "AUD"))
    val availableCurrencies: StateFlow<List<String>> = _availableCurrencies

    init {
        fetchExchangeRates(_selectedBaseCurrency.value)
    }

    fun fetchExchangeRates(baseCurrency: String) {
        viewModelScope.launch {
            _exchangeRates.value = Result.Loading // Indicate loading state
            val result = repository.getLatestRates(baseCurrency, API_KEY)
            _exchangeRates.value = result
        }
    }

    fun selectBaseCurrency(currencyCode: String) {
        _selectedBaseCurrency.value = currencyCode
        fetchExchangeRates(currencyCode) // Fetch new rates when base currency changes
    }
}

// Sealed class to represent the state of the data
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

// ViewModel Factory to create the ViewModel with the repository dependency
class CurrencyViewModelFactory(private val repository: CurrencyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrencyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CurrencyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}