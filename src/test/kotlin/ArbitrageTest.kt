package org.kevin

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kevin.enums.OrderType
import org.kevin.models.Order
import org.kevin.services.ArbitrageDetector
import org.kevin.services.ExchangeBook

class ArbitrageTest {
    lateinit var exA: ExchangeBook
    lateinit var exB: ExchangeBook
    lateinit var detector: ArbitrageDetector

    @BeforeEach
    fun setup() {
        exA = ExchangeBook("A")
        exB = ExchangeBook("B")
        detector = ArbitrageDetector(exA, exB)
    }

    @Test
    fun testArbitrageDetection(){
        exA.addOrder(Order("1", OrderType.SELL, 13.0, 1.0))
        exA.addOrder(Order("2", OrderType.BUY, 29.0, 1.0))

        exB.addOrder(Order("3", OrderType.BUY, 24.0, 1.0))
        exB.addOrder(Order("4", OrderType.SELL, 12.0, 1.0))
        val deals = detector.findAll()

        assertEquals(2, deals.size)
        assertEquals("B", deals[0].buyExchange)
        assertEquals("A", deals[0].sellExchange)
        assertEquals(12.0, deals[0].buyPrice)
        assertEquals(29.0, deals[0].sellPrice)
    }

    @Test
    fun testRemoveOrder(){
        exA.addOrder(Order("1", OrderType.SELL, 13.0, 1.0))
        exA.cancelOrder("1")

        assertEquals(null, exA.bestAsk())
    }
    @Test
    fun testNoArbitrage(){
        exA.addOrder(Order("1", OrderType.SELL, 15.0, 1.0))
        exA.addOrder(Order("2", OrderType.BUY, 20.0, 1.0))

        exB.addOrder(Order("3", OrderType.BUY, 14.0, 1.0))
        exB.addOrder(Order("4", OrderType.SELL, 18.0, 1.0))
        val deals = detector.findAll()

        assertEquals(0, deals.size)
    }
    @Test
    fun costToBuy(){
        exA.addOrder(Order("1", OrderType.SELL, 13.0, 1.0))
        exA.addOrder(Order("2", OrderType.SELL, 24.0, 1.0))
        exA.addOrder(Order("3", OrderType.SELL, 19.0, 5.0))
        exA.addOrder(Order("4", OrderType.SELL, 12.0, 1.0))

        assertEquals(12.0, exA.costToBuy(1.0))
        assertEquals(25.0, exA.costToBuy(2.0))
        assertEquals(34.5, exA.costToBuy(2.5))
        assertEquals(null, exA.costToBuy(9.0))

    }

}