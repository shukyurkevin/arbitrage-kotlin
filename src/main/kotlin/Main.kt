package org.kevin

import jdk.internal.classfile.components.ClassPrinter.*
import org.kevin.enums.OrderType
import org.kevin.models.Order
import org.kevin.services.ArbitrageDetector
import org.kevin.services.ExchangeBook
import java.util.LinkedList

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val exA  = ExchangeBook("A")
    val exB = ExchangeBook("B")
    val detector = ArbitrageDetector(exA, exB)

    exA.addOrder(Order("1", OrderType.SELL,13.0,1.0))
    exA.addOrder(Order("2", OrderType.BUY,29.0,1.0))

    exB.addOrder(Order("3", OrderType.BUY,24.0,1.0))
    exB.addOrder(Order("4", OrderType.SELL,12.0,1.0))

    println(exA.bestBid())
    println(exB.bestBid())
    println(exA.bestAsk())
    println(exB.bestAsk())

    val deals = detector.findAll()

}