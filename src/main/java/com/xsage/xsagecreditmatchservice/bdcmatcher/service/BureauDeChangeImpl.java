package com.xsage.xsagecreditmatchservice.bdcmatcher.service;

import com.xsage.xsagecreditmatchservice.shared.util.SupportedCurrency;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BureauDeChangeImpl implements BureauDeChange{
    private static final double MAX_DISTANCE_KM = 3.0;
    private static final int MAX_RESULTS = 15;
    private final Map<SupportedCurrency, NavigableSet<Peer>> peerBook = new ConcurrentHashMap<>(); //Todo: have this populate from a store
    @Override
    public NavigableSet<Peer> findNearestExchangePeer(Location location, BigDecimal amount, SupportedCurrency currency) {
        return peerBook.getOrDefault(currency, new TreeSet<>(Comparator.comparing(Peer::balance)))
                .stream()
                .filter(peer -> peer.location().isWithinRange(location, MAX_DISTANCE_KM))
                .filter(peer -> peer.balance().compareTo(amount) >= 0)
                .limit(MAX_RESULTS)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Peer::balance))));
    }

    @Override
    public NavigableSet<Peer> match(Peer requester, Location location, BigDecimal amount, SupportedCurrency currency) {
        return findNearestExchangePeer(location, amount, currency);
    }

    @Override
    public boolean handleExchangeTransaction(Object exchangeData) {
        System.out.println("Message received");
        return false;
    }

    @Override
    public void merchantGrader() {
        throw new UnsupportedOperationException("This method is not yet implemented.");
    }


    @Override
    public Double getMerchantGrade() {
        throw new UnsupportedOperationException("This method is not yet implemented.");
    }
}
