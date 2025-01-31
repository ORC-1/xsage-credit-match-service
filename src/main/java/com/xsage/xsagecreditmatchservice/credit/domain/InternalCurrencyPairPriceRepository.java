package com.xsage.xsagecreditmatchservice.credit.domain;

import com.xsage.xsagecreditmatchservice.shared.util.ValidCurrencyPair;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternalCurrencyPairPriceRepository extends CrudRepository<InternalCurrencyPairPrice, Long> {
    InternalCurrencyPairPrice getInternalCurrencyPairPriceByValidCurrencyPair(ValidCurrencyPair validCurrencyPair);
}
