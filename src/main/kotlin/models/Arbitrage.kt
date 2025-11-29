package org.kevin.models

data class Arbitrage (
    val buyExchange: String,
    val sellExchange: String,
    val buyPrice: Double,
    val sellPrice: Double,
    val profitPerUnit: Double
)