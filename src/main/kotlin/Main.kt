package org.kevin

import org.kevin.enums.OrderType
import org.kevin.data.Order
import org.kevin.services.ArbitrageDetector
import org.kevin.services.ExchangeBook
import org.kevin.services.OrderGenerator

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
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

    val deals = detector.findAll()

    deals.forEach {println(it)}


}