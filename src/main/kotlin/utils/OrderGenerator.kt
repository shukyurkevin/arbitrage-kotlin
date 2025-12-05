package org.kevin.utils

import org.kevin.data.Order
import org.kevin.enums.OrderType
import kotlin.random.Random

class OrderGenerator {

    fun randomOrder(): Order {
        val id = Random.nextInt(1, 101).toString()
        val type = if (Random.nextBoolean()) OrderType.BUY else OrderType.SELL
        val price = Random.nextInt(10, 100).toDouble()
        val quantity = Random.nextInt(1, 20).toDouble()
        return Order(id, type, price, quantity)
    }

    fun randomOrders(count: Int): List<Order> =
        List(count) { randomOrder() }
}