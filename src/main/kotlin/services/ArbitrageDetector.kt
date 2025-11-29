package org.kevin.services

import org.kevin.models.Arbitrage

class ArbitrageDetector(private val exchangeA: ExchangeBook, private val exchangeB: ExchangeBook) {

    fun findOpportunity(): Arbitrage? {
        val aBestBid = exchangeA.bestBid()
        val aBestAsk = exchangeA.bestAsk()
        val bBestBid = exchangeB.bestBid()
        val bBestAsk = exchangeB.bestAsk()

        if (aBestBid == null || aBestAsk == null || bBestBid == null || bBestAsk == null) return null

        if (aBestBid > bBestAsk) {
            return Arbitrage(
                buyExchange = exchangeB.name,
                sellExchange = exchangeA.name,
                buyPrice = bBestAsk,
                sellPrice = aBestBid,
                profitPerUnit = aBestBid - bBestAsk
            )
        }

        if (bBestBid > aBestAsk) {
            return Arbitrage(
                buyExchange = exchangeA.name,
                sellExchange = exchangeB.name,
                buyPrice = aBestAsk,
                sellPrice = bBestBid,
                profitPerUnit = bBestBid - aBestAsk
            )
        }
        return null
    }
}