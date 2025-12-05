package org.kevin

import org.kevin.services.ArbitrageDetector
import org.kevin.services.ExchangeBook
import org.kevin.utils.OrderGenerator

fun main() {
    val exA  = ExchangeBook("A")
    val exB = ExchangeBook("B")
    val detector = ArbitrageDetector(exA, exB)
    val generator = OrderGenerator()
    val manyOrders = generator.randomOrders(10)

    manyOrders.forEach { order ->
        exA.addOrder(order)
    }
    val manyOrdersB = generator.randomOrders(10)

    manyOrdersB.forEach { order ->
        exB.addOrder(order)
    }
    println(exA)
    println(exB)

    val deals = detector.findAllV2()

    deals.forEach {println(it)}
}