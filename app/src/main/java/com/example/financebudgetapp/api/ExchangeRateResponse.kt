package com.example.financebudgetapp.api

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    val result: String,
    @SerializedName("base_code") val baseCode: String,
    @SerializedName("conversion_rates") val conversionRates: Map<String, Double>,
    // You might include other fields like time_last_update_utc if needed
)

// You might still want a data class for a single currency if you plan to display a list
data class Currency(
    val code: String,
    val name: String // You'll need a separate source for full names
)