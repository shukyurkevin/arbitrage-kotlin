package org.kevin.data

data class Arbitrage (
    val buyExchange: String,
    val sellExchange: String,
    val buyPrice: Double,
    val sellPrice: Double,
    val profitPerUnit: Double,
    val profitQty: Double,
)