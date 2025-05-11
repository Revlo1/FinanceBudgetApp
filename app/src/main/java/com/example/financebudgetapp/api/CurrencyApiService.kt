package com.example.financebudgetapp.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApiService {

    @GET("{apiKey}/latest/{baseCurrency}")
    suspend fun getLatestRates(
        @Path("apiKey") apiKey: String,
        @Path("baseCurrency") baseCurrency: String
    ): Response<ExchangeRateResponse>

}