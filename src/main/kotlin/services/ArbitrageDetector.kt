package org.kevin.services

import org.kevin.models.Arbitrage
import org.kevin.models.Order

class ArbitrageDetector(private val exchangeA: ExchangeBook, private val exchangeB: ExchangeBook) {

    fun findOpportunity(): Arbitrage? {
        val aBestBid = exchangeA.bestBid()
        val aBestAsk = exchangeA.bestAsk()
        val bBestBid = exchangeB.bestBid()
        val bBestAsk = exchangeB.bestAsk()
        println("DEBUG findOpportunity -> A: bid=$aBestBid ask=$aBestAsk  B: bid=$bBestBid ask=$bBestAsk")


        if (aBestBid != null && bBestAsk != null && aBestBid > bBestAsk) {
            return Arbitrage(
                buyExchange = exchangeB.name,
                sellExchange = exchangeA.name,
                buyPrice = bBestAsk,
                sellPrice = aBestBid,
                profitPerUnit = aBestBid - bBestAsk
            )
        }

        if (bBestBid != null && aBestAsk != null && bBestBid > aBestAsk){
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
    fun findAll() : List<Arbitrage> {
        val results = mutableListOf<Arbitrage>()

        while(true){
            val arb = findOpportunity()?: break
            results.add(arb)

            if (arb.buyExchange == exchangeA.name){
                exchangeA.removeBestSell()
                exchangeB.removeBestBid()
            }else{
                exchangeB.removeBestSell()
                exchangeA.removeBestBid()
            }
        }
        return results
    }
    fun findBestBid(exchanges: List<ExchangeBook>): Double? {
        var bestBid: Double? = null
        for (exchange in exchanges) {
            val exchangeBestBid = exchange.bestBid()
            if (exchangeBestBid != null) {
                if (bestBid == null || exchangeBestBid > bestBid) {
                    bestBid = exchangeBestBid
                }
            }
        }
        return bestBid

    }
    fun findBestAsk(exchanges: List<ExchangeBook>): Double? {
        var bestAsk: Double? = null
        for (exchange in exchanges) {
            val exchangeBestAsk = exchange.bestAsk()
            if (exchangeBestAsk != null) {
                if (bestAsk == null || exchangeBestAsk < bestAsk) {
                    bestAsk = exchangeBestAsk
                }
            }
        }
        return bestAsk

    }

}