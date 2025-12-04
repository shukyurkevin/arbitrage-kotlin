package org.kevin.services

import org.kevin.data.Arbitrage

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
                profitPerUnit = aBestBid - bBestAsk,
                profitQty = 1.0
            )
        }

        if (bBestBid != null && aBestAsk != null && bBestBid > aBestAsk){
            return Arbitrage(
                buyExchange = exchangeA.name,
                sellExchange = exchangeB.name,
                buyPrice = aBestAsk,
                sellPrice = bBestBid,
                profitPerUnit = bBestBid - aBestAsk,
                profitQty = 1.0
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

    fun findOpportunityV2(): Arbitrage? {
        val aBestBid = exchangeA.bestBidOrder()
        val bBestBid = exchangeB.bestBidOrder()
        val bBestAsk = exchangeB.bestAskOrder()
        val aBestAsk = exchangeB.bestAskOrder()

        if (aBestBid != null && bBestAsk != null && aBestBid.price > bBestAsk.price){
            if (bBestAsk.quantity >= aBestBid.quantity) {
                return Arbitrage(
                    buyExchange = exchangeA.name,
                    sellExchange = exchangeB.name,
                    buyPrice = bBestAsk.price,
                    sellPrice = aBestBid.price,
                    profitPerUnit = aBestBid.price - bBestAsk.price,
                    profitQty = bBestAsk.quantity - aBestBid.quantity
                )
            }else{
                println("found opportunity but not enough for big profit")
                return null
            }
        }
        if (bBestBid != null && aBestAsk != null && bBestBid.price > aBestAsk.price) {
            if (aBestAsk.quantity >= bBestBid.quantity) {
                return Arbitrage(
                    buyExchange = exchangeA.name,
                    sellExchange = exchangeB.name,
                    buyPrice = aBestAsk.price,
                    sellPrice = bBestBid.price,
                    profitPerUnit = bBestBid.price - aBestAsk.price,
                    profitQty = aBestAsk.quantity - bBestBid.quantity
                )
            }else{
                println("found opportunity but not enough for big profit")
                return null
            }
        }
        return null
    }
}