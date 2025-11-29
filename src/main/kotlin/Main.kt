package org.kevin

import org.kevin.enums.OrderType
import org.kevin.models.Order
import org.kevin.services.ArbitrageDetector
import org.kevin.services.ExchangeBook

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val exA  = ExchangeBook("A")
    val exB = ExchangeBook("B")
    val detector = ArbitrageDetector(exA, exB)

    exA.addOrder(Order("1", OrderType.BUY,20.0,1.0))
    exA.addOrder(Order("2", OrderType.SELL,15.0,1.0))

    exB.addOrder(Order("3", OrderType.BUY,25.0,1.0))
    exB.addOrder(Order("4", OrderType.SELL,12.0,1.0))

    while (true) {
        println(detector.findOpportunity())
        Thread.sleep(1000)
    }
}