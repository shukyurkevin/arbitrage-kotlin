package org.kevin.services

import org.kevin.enums.OrderType
import org.kevin.models.Order
import java.util.TreeMap

class ExchangeBook(val name: String) {
    private val buyOrders : TreeMap<Double, MutableList<Order>> = TreeMap(reverseOrder())
    private val sellOrders : TreeMap<Double, MutableList<Order>> = TreeMap()
    private val ordersById : MutableMap<String, Order> = mutableMapOf()

    @Synchronized
    fun addOrder(order: Order) {

        val map = (if (order.type == OrderType.BUY) buyOrders else sellOrders)
            .computeIfAbsent(order.price) { mutableListOf() }
        map.add(order)
        ordersById[order.id] = order

    }

    @Synchronized
    fun cancelOrder(orderId: String) {

        val order = ordersById.remove(orderId) ?: return
        val map = if (order.type == OrderType.BUY) buyOrders else sellOrders
        val list = map[order.price] ?: return
        list.removeIf { it.id == orderId }
        if (list.isEmpty()) map.remove(order.price)

    }

    @Synchronized
    fun bestBid(): Double? = buyOrders.firstEntry()?.key

    @Synchronized
    fun bestAsk(): Double? = sellOrders.firstEntry()?.key

}