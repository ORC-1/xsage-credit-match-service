package com.xsage.xsagecreditmatchservice.bdcmatcher.service;

import com.xsage.xsagecreditmatchservice.credit.domain.Merchant;

import java.util.UUID;

public record ExchangeOrder(Merchant merchant, Peer peer, UUID orderId) {
}
