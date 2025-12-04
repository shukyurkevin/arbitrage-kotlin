package org.kevin

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.kevin.data.Order
import org.kevin.enums.OrderType
import org.kevin.services.ExchangeBook

class ExchangeBookTest {
    lateinit var exchange: ExchangeBook

    @BeforeEach
    fun setup() {
        exchange = ExchangeBook("Kraken")
    }

    @Test
    fun testRemoveOrder(){
        exchange.addOrder(Order("1", OrderType.SELL, 13.0, 1.0))
        exchange.cancelOrder("1")

        assertEquals(null, exchange.bestAsk())
    }

    @Test
    fun costToBuy(){
        exchange.addOrder(Order("1", OrderType.SELL, 13.0, 1.0))
        exchange.addOrder(Order("2", OrderType.SELL, 24.0, 1.0))
        exchange.addOrder(Order("3", OrderType.SELL, 19.0, 5.0))
        exchange.addOrder(Order("4", OrderType.SELL, 12.0, 1.0))

        assertEquals(12.0, exchange.costToBuy(1.0))
        assertEquals(25.0, exchange.costToBuy(2.0))
        assertEquals(34.5, exchange.costToBuy(2.5))
        assertEquals(null, exchange.costToBuy(9.0))

    }

    @Test
    fun costToSell(){
        exchange.addOrder(Order("1", OrderType.BUY, 13.0, 1.0))
        exchange.addOrder(Order("2", OrderType.BUY, 24.0, 1.0))
        exchange.addOrder(Order("3", OrderType.BUY, 19.0, 5.0))
        exchange.addOrder(Order("4", OrderType.BUY, 12.0, 1.0))

        assertEquals(24.0, exchange.costToSell(1.0))
        assertEquals(43.0, exchange.costToSell(2.0))
        assertEquals(52.5, exchange.costToSell(2.5))
        assertEquals(null, exchange.costToSell(9.0))

    }
}