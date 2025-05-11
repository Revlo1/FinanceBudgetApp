package com.example.financebudgetapp.api

class CurrencyRepository(private val apiService: CurrencyApiService) {

    suspend fun getLatestRates(baseCurrency: String, apiKey: String): Result<ExchangeRateResponse> {
        return try {
            val response = apiService.getLatestRates(apiKey, baseCurrency) //API Key, GBP
            if (response.isSuccessful) {
                response.body()?.let {
                    if (it.result == "success") {
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
}