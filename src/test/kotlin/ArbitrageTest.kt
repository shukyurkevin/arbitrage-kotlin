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
}