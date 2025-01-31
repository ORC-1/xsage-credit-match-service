package com.xsage.xsagecreditmatchservice.credit.domain;

import com.xsage.xsagecreditmatchservice.shared.util.ValidCurrencyPair;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
    Product getProductByTradingPairAndMerchantId(ValidCurrencyPair validCurrencyPair, Long merchantId);
}
