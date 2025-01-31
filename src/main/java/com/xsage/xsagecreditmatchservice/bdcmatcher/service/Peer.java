package com.xsage.xsagecreditmatchservice.bdcmatcher.service;

import com.xsage.xsagecreditmatchservice.shared.util.SupportedCurrency;

import java.math.BigDecimal;

record Peer(String name, Location location, BigDecimal balance, SupportedCurrency currency) {}
