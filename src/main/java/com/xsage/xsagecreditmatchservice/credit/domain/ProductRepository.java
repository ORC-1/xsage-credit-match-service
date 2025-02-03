package com.xsage.xsagecreditmatchservice.credit.domain;

import com.xsage.xsagecreditmatchservice.shared.util.ValidCurrencyPair;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {
    Product getProductByTradingPairAndMerchantId(ValidCurrencyPair validCurrencyPair, Long merchantId);

    boolean existsByMerchantIdAndTradingPair(Merchant merchant, ValidCurrencyPair tradingPair); // Efficient existence check
    Optional<Product> findByMerchantIdAndTradingPair(Merchant merchant, ValidCurrencyPair tradingPair);

    List<Product> findByTradingPair(ValidCurrencyPair currencyPair);
}
