package com.xsage.xsagecreditmatchservice.bdcmatcher.service;

import com.xsage.xsagecreditmatchservice.credit.domain.*;
import com.xsage.xsagecreditmatchservice.credit.service.ExchangeTransaction;
import com.xsage.xsagecreditmatchservice.shared.util.ValidCurrencyPair;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BureauDeChangeImpl implements BureauDeChange {
    private static final double MAX_DISTANCE_KM = 3.0;
    private static final int MAX_RESULTS = 15;
    private final MerchantBankInfoRepository merchantBankInfoRepository;
    private final ProductRepository productRepository;
    private static final Logger log = LoggerFactory.getLogger(BureauDeChangeImpl.class);


    public NavigableSet<Merchant> findNearestExchangeMerchant(Location location, BigDecimal amount, ValidCurrencyPair currencyPair) {
        return productRepository.findByTradingPair(currencyPair).stream()
                .map(Product::getMerchantId)
                .filter(merchant -> isWithinRange(merchant, location))
                .filter(merchant -> hasSufficientBalance(merchant, amount))
                .filter(this::isEligibleForTransaction)
                .sorted(Comparator.comparingDouble(merchant -> calculateRateDelta(merchant, currencyPair)))
                .limit(MAX_RESULTS)
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(Merchant::getPenalty))));
    }

    private boolean isWithinRange(Merchant merchant, Location location) {
        return merchant.getLatitude() != null && merchant.getLongitude() != null &&
               location.isWithinRange(new Location(merchant.getLatitude(), merchant.getLongitude()), MAX_DISTANCE_KM);
    }

    private boolean hasSufficientBalance(Merchant merchant, BigDecimal amount) {
        Optional<MerchantBankInfo> bankInfo = merchantBankInfoRepository.findByMerchantId(merchant);
        return bankInfo.isPresent() &&
               BigDecimal.valueOf(bankInfo.get().getBalance()).compareTo(amount) >= 0;
    }

    private boolean isEligibleForTransaction(Merchant merchant) {
        int MIN_PENALTY_SCORE = 30; //TODO: MIN_PENALTY_SCORE should be dynamic
        double penaltyScore = merchant.getPenalty() != null ? merchant.getPenalty().getEnumValue() : 0;
        double returnRate = merchant.getCustomerRepeatRate() != null ? merchant.getCustomerRepeatRate() : 0;
        return (100 - penaltyScore) >= MIN_PENALTY_SCORE && returnRate >= MIN_PENALTY_SCORE;
    }

    private double calculateRateDelta(Merchant merchant, ValidCurrencyPair currencyPair) {
        Product product = productRepository.findByMerchantIdAndTradingPair(merchant, currencyPair).stream().findFirst().orElse(null);

        if (product == null) {
            return Double.MAX_VALUE;
        }
        return Math.abs(product.getSellRate() - product.getBuyRate());
    }

    @Override
    public NavigableSet<Merchant> match(Peer requester, Location location, BigDecimal amount, ValidCurrencyPair currency) {
        return findNearestExchangeMerchant(location, amount, currency);
    }

    @Override
    public void handleExchangeTransaction(ExchangeTransaction exchangeData) {
        log.info("Message received: Completing Exchange-Find Merchant");

        Peer user = new Peer(exchangeData.userid(),
                exchangeData.location(),
                exchangeData.amount(),
                exchangeData.currency());

        match(user,
                exchangeData.location(),
                exchangeData.amount(),
                exchangeData.currency());

        //TODO: Commit result to store and emit event which updates and shows the top 3 options to the User, who then selects


    }

    @Override
    public void orderExecution(ExchangeOrder exchangeOrder) {
        throw new UnsupportedOperationException("This method is not yet implemented.");
    }

    @Override
    public void merchantGrader() {
        throw new UnsupportedOperationException("This method is not yet implemented.");
    }

}
