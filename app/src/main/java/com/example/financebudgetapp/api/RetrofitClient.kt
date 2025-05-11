package com.example.financebudgetapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    //base URL for exchange rate API
    private const val BASE_URL = "https://v6.exchangerate-api.com/v6/"

    val currencyApiService: CurrencyApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApiService::class.java)
    }
}