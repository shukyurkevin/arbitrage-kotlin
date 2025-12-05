package org.kevin.services

import org.kevin.data.Arbitrage
import kotlin.math.min

class ArbitrageDetector(private val exchangeA: ExchangeBook, private val exchangeB: ExchangeBook) {

    fun findOpportunity(): Arbitrage? {
        val aBestBid = exchangeA.bestBid()
        val aBestAsk = exchangeA.bestAsk()
        val bBestBid = exchangeB.bestBid()
        val bBestAsk = exchangeB.bestAsk()

        if (aBestBid != null && bBestAsk != null && aBestBid > bBestAsk) {
            if ((aBestBid - bBestAsk > bBestAsk / 10)) {
                return Arbitrage(
                    buyExchange = exchangeB.name,
                    sellExchange = exchangeA.name,
                    buyPrice = bBestAsk,
                    sellPrice = aBestBid,
                    profitPerUnit = aBestBid - bBestAsk,
                    profitQty = 1.0
                )
            }else null
        }
        if (bBestBid != null && aBestAsk != null && bBestBid > aBestAsk){
            if (bBestBid - aBestAsk > bBestBid / 10){
            return Arbitrage(
                buyExchange = exchangeA.name,
                sellExchange = exchangeB.name,
                buyPrice = aBestAsk,
                sellPrice = bBestBid,
                profitPerUnit = bBestBid - aBestAsk,
                profitQty = 1.0
            )
        }else null
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
//        val validResults = results.filter {it.profitQty > 0}
//        val listsOfResults = results.partition {it.profitQty > 0}
        return results
    }
    fun findAllV2() : List<Arbitrage> {
        val results = mutableListOf<Arbitrage>()

        while(true){
            val arb = findOpportunityV2()?: break
            results.add(arb)

            if (arb.buyExchange == exchangeA.name){
                exchangeA.removeBestSellV2(arb.profitQty)
                exchangeB.removeBestBidV2(arb.profitQty)
            }else{
                exchangeB.removeBestSellV2(arb.profitQty)
                exchangeA.removeBestBidV2(arb.profitQty)
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
        val aBestAsk = exchangeA.bestAskOrder()

        if (aBestBid != null && bBestAsk != null && aBestBid.price > bBestAsk.price){
            if ((aBestBid.price - bBestAsk.price) > (bBestAsk.price/10) ) {
                return Arbitrage(
                    buyExchange = exchangeA.name,
                    sellExchange = exchangeB.name,
                    buyPrice = bBestAsk.price,
                    sellPrice = aBestBid.price,
                    profitPerUnit = aBestBid.price - bBestAsk.price,
                    profitQty = min(bBestAsk.quantity,aBestBid.quantity)
                )
            }else{
                println("found opportunity but not enough for big profit")
                return null
            }
        }
        if (bBestBid != null && aBestAsk != null && bBestBid.price > aBestAsk.price) {
            if ((bBestBid.price - aBestAsk.price) > (aBestAsk.price/10)) {
                return Arbitrage(
                    buyExchange = exchangeA.name,
                    sellExchange = exchangeB.name,
                    buyPrice = aBestAsk.price,
                    sellPrice = bBestBid.price,
                    profitPerUnit = bBestBid.price - aBestAsk.price,
                    profitQty = min(bBestBid.quantity, aBestAsk.quantity),
                )
            }else{
                println("found opportunity but not enough for big profit")
                return null
            }
        }
        return null
    }
}