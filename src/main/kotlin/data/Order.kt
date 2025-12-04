package org.kevin.data

import org.kevin.enums.OrderType

data class Order(
    val id: String,
    val type: OrderType,
    val price: Double,
    val quantity: Double
)