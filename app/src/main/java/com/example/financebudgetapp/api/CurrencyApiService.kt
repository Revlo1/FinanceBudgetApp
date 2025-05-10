package com.example.financebudgetapp.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApiService {

    @GET("{apiKey}/latest/{baseCurrency}") // Endpoint structure for ExchangeRate-API
    suspend fun getLatestRates(
        @Path("apiKey") apiKey: String,
        @Path("baseCurrency") baseCurrency: String
    ): Response<ExchangeRateResponse>

    // ExchangeRate-API also has an endpoint to get a list of supported currencies:
    // @GET("{apiKey}/codes")
    // suspend fun getSupportedCurrencies(@Path("apiKey") apiKey: String): Response<SupportedCurrenciesResponse>
}