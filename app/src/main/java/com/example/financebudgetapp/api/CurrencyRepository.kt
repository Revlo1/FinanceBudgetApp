package com.example.financebudgetapp.api

import com.example.financebudgetapp.api.CurrencyApiService
import com.example.financebudgetapp.api.ExchangeRateResponse
//import com.example.financebudgetapp.ui.currency.Result // Import the Result sealed class

class CurrencyRepository(private val apiService: CurrencyApiService) {

    suspend fun getLatestRates(baseCurrency: String, apiKey: String): Result<ExchangeRateResponse> {
        return try {
            val response = apiService.getLatestRates(apiKey, baseCurrency) // Pass apiKey first
            if (response.isSuccessful) {
                response.body()?.let {
                    if (it.result == "success") { // Check the 'result' field
                        Result.Success(it)
                    } else {
                        Result.Failure(Exception("API returned error: ${it.result}"))
                    }
                } ?: Result.Failure(Exception("Empty response body"))
            } else {
                Result.Failure(Exception("API request failed: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    // You might add a function to fetch supported currencies if needed
    // suspend fun getSupportedCurrencies(apiKey: String): Result<SupportedCurrenciesResponse> { ... }
}