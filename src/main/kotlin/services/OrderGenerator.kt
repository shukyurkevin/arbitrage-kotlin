package org.kevin.services

import org.kevin.data.Order
import org.kevin.enums.OrderType
import kotlin.random.Random
import java.util.UUID

class OrderGenerator {

    fun randomOrder(): Order {
        val id = UUID.randomUUID().toString()
        val type = if (Random.nextBoolean()) OrderType.BUY else OrderType.SELL
        val price = Random.nextDouble(10.0, 100.0)   // диапазон цены 10–100
        val quantity = 1.0

        return Order(id, type, price, quantity)
    }

    fun randomOrders(count: Int): List<Order> =
        List(count) { randomOrder() }
}