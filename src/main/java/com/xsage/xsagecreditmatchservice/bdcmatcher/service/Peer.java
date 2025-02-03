package com.xsage.xsagecreditmatchservice.bdcmatcher.service;

import com.xsage.xsagecreditmatchservice.shared.util.ValidCurrencyPair;

import java.math.BigDecimal;

public record Peer(String name, Location location, BigDecimal balance, ValidCurrencyPair currency) {}
