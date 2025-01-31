package com.xsage.xsagecreditmatchservice.bdcmatcher.service;

import com.xsage.xsagecreditmatchservice.shared.util.SupportedCurrency;

import java.math.BigDecimal;
import java.util.NavigableSet;

public interface BureauDeChange {

    /**
     * Finds the nearest exchange peer.
     *
     * @param location The location to search for peers.
     * @param amount   The amount involved in the exchange.
     * @param currency The currency of the exchange.
     * @return A list of merchants matching the filter criteria (location, balance, penalty, and previous customer return rate).
     */
    NavigableSet<Peer> findNearestExchangePeer(Location location, BigDecimal amount, SupportedCurrency currency);
    NavigableSet<Peer> match(Peer requester, Location location, BigDecimal amount, SupportedCurrency currency);
    boolean handleExchangeTransaction(Object exchangeData);
    void merchantGrader();
    Double getMerchantGrade();
}
