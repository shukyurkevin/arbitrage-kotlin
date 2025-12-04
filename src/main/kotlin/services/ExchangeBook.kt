package org.kevin.services

import org.kevin.enums.OrderType
import org.kevin.models.Order
import java.util.TreeMap
import kotlin.math.min

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
    fun removeBestBid(): Order?{
        val bestBid = buyOrders.firstKey() ?: return null
        val orders = buyOrders[bestBid]!!

        var removedOrder = orders.removeFirst()

        if (orders.isEmpty()){
            buyOrders.remove(bestBid)
        }
        ordersById.remove(removedOrder.id)
        return removedOrder
    }

    @Synchronized
    fun removeBestSell(): Order?{
        val bestSell = sellOrders.firstKey() ?: return null
        val orders = sellOrders[bestSell]!!

        var removedOrder = orders.removeFirst()

        if (orders.isEmpty()){
            sellOrders.remove(bestSell)
        }
        ordersById.remove(removedOrder.id)
        return removedOrder
    }
    @Synchronized
    fun costToBuy(quantity: Double): Double? {

        var cost = 0.0;
        var remainingQuantity = quantity;

        for ((price,orders) in sellOrders){
            if (orders.isEmpty()) continue

            for (order in orders){
                val qtyToBuy = min(order.quantity, remainingQuantity)
                cost += qtyToBuy * price
                remainingQuantity -= qtyToBuy

                if (remainingQuantity <= 0.0){
                    println(cost)
                    return cost
                }
            }
        }
        println("not enough quantity to buy $quantity")
        return null
    }


    @Synchronized
    fun bestBid(): Double? = buyOrders.firstEntry()?.key

    @Synchronized
    fun bestAsk(): Double? = sellOrders.firstEntry()?.key

}