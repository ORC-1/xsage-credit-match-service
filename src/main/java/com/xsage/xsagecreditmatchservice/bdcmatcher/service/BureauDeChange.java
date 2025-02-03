package com.xsage.xsagecreditmatchservice.bdcmatcher.service;

import com.xsage.xsagecreditmatchservice.credit.domain.Merchant;
import com.xsage.xsagecreditmatchservice.credit.service.ExchangeTransaction;
import com.xsage.xsagecreditmatchservice.shared.util.ValidCurrencyPair;

import java.math.BigDecimal;
import java.util.NavigableSet;

public interface BureauDeChange {

    /**
     * Finds the nearest merchant peer.
     *
     * @param location The location to search for peers.
     * @param amount   The amount involved in the exchange.
     * @param currency The currency of the exchange.
     * @return A list of merchants matching the filter criteria (location, balance, penalty, and previous customer return rate).
     */
    NavigableSet<Merchant> findNearestExchangeMerchant(Location location, BigDecimal amount, ValidCurrencyPair currency);

    /**
     * Match the top 3 Merchant.
     *
     * @param requester The Exchange Order requester.
     * @param location The location to search for peers.
     * @param amount   The amount involved in the exchange.
     * @param currencyPair The currency pair for the exchange.
     * @return top 3 list of merchants matching the filter criteria (location, balance, penalty, and previous customer return rate).
     */
    NavigableSet<Merchant> match(Peer requester, Location location, BigDecimal amount, ValidCurrencyPair currencyPair);

    /**
     * Handles the details of an initiated exchange transaction.  This includes logging,
     * updating balances, and triggering other downstream processes like orderExecution.
     *
     * @param exchangeData The data associated with the initiated exchange transaction.
     */
    void handleExchangeTransaction(ExchangeTransaction exchangeData);

    /**
     * Executes an exchange order, involving debiting the chosen
     * merchant position, and updating the status of the order.
     *
     * @param exchangeOrder The exchange order to be executed.
     */
    void orderExecution(ExchangeOrder exchangeOrder);

    /**
     * Periodically grades merchants based on performance criteria. This method is
     * called by a scheduler every 24 Hours or EOB.
     */
    void merchantGrader();
}
