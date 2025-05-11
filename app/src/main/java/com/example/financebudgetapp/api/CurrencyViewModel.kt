package com.example.financebudgetapp.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CurrencyViewModel(private val repository: CurrencyRepository) : ViewModel() {

    private val _exchangeRates = MutableStateFlow<Result<ExchangeRateResponse>>(Result.Loading)
    val exchangeRates: StateFlow<Result<ExchangeRateResponse>> = _exchangeRates

    private val _selectedBaseCurrency = MutableStateFlow("GBP")
    val selectedBaseCurrency: StateFlow<String> = _selectedBaseCurrency

    private val API_KEY = "c25e5dc8fc7e760e3eb59da5"

    //currencies
    private val _availableCurrencies = MutableStateFlow(listOf("GBP", "EUR", "USD", "CAD", "AUD", "JPY", "NZD", "CHF", "CNY"))
    val availableCurrencies: StateFlow<List<String>> = _availableCurrencies

    init {
        fetchExchangeRates(_selectedBaseCurrency.value)
    }

    private fun fetchExchangeRates(baseCurrency: String) {
        viewModelScope.launch {
            _exchangeRates.value = Result.Loading
            val result = repository.getLatestRates(baseCurrency, API_KEY)
            _exchangeRates.value = result
        }
    }

    fun selectBaseCurrency(currencyCode: String) {
        _selectedBaseCurrency.value = currencyCode
        fetchExchangeRates(currencyCode)
    }
}

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val exception: Exception) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}

class CurrencyViewModelFactory(private val repository: CurrencyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrencyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CurrencyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}