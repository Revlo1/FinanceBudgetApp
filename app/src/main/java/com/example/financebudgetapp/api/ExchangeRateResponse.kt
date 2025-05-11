package com.example.financebudgetapp.api

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    val result: String,
    @SerializedName("base_code") val baseCode: String,
    @SerializedName("conversion_rates") val conversionRates: Map<String, Double>,
)

data class Currency(
    val code: String,
    val name: String
)