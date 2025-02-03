package com.xsage.xsagecreditmatchservice.credit.service;

import com.xsage.xsagecreditmatchservice.bdcmatcher.service.Location;
import com.xsage.xsagecreditmatchservice.shared.util.ValidCurrencyPair;

import java.math.BigDecimal;

public record ExchangeTransaction(String userid, Location location, BigDecimal amount, ValidCurrencyPair currency) {
}
