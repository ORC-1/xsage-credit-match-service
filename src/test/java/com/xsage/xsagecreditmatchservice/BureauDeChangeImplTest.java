package com.xsage.xsagecreditmatchservice;

import com.xsage.xsagecreditmatchservice.bdcmatcher.service.BureauDeChangeImpl;
import com.xsage.xsagecreditmatchservice.bdcmatcher.service.Location;
import com.xsage.xsagecreditmatchservice.bdcmatcher.service.Peer;
import com.xsage.xsagecreditmatchservice.credit.domain.*;
import com.xsage.xsagecreditmatchservice.credit.service.ExchangeTransaction;
import com.xsage.xsagecreditmatchservice.shared.util.PenaltyWeight;
import com.xsage.xsagecreditmatchservice.shared.util.ValidCurrencyPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BureauDeChangeImplTest {
    @Mock
    private MerchantBankInfoRepository merchantBankInfoRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private BureauDeChangeImpl bureauDeChange;

    private final ValidCurrencyPair currencyPair = ValidCurrencyPair.USD_NGN;
    private final Location location = new Location(6.5244, 3.3792);
    private final Merchant merchant = new Merchant("merchant1", 6.5245, 3.3793, PenaltyWeight.SLOW_DELIVERY);
    private final MerchantBankInfo merchantBankInfo = new MerchantBankInfo(merchant, 1000.0);
    private final Product product = new Product(merchant, currencyPair, 700.0, 680.0);

    @BeforeEach
    void setUp() {
        merchant.setCustomerRepeatRate(70.2);
        bureauDeChange = new BureauDeChangeImpl(merchantBankInfoRepository, productRepository);
    }

    @Test
    void testFindNearestExchangePeer_Success() {
        List<Product> products = List.of(product);
        when(productRepository.findByTradingPair(currencyPair))
                .thenReturn(products);

        when(merchantBankInfoRepository.findByMerchantId(merchant))
                .thenReturn(Optional.of(merchantBankInfo));

        NavigableSet<Merchant> result = bureauDeChange.findNearestExchangeMerchant(location, BigDecimal.valueOf(500), currencyPair);
        assertFalse(result.isEmpty());
        assertTrue(result.contains(merchant));
    }

    @Test
    void testFindNearestExchangePeerNoMerchantFound() {
        NavigableSet<Merchant> result = bureauDeChange.findNearestExchangeMerchant(location, BigDecimal.valueOf(500), currencyPair);
        assertTrue(result.isEmpty());
    }

    @Test
    void testNoMerchantWithLowReturnIsGivenInFindNearestExchange() {
        double LOW_RETURN_RATE = 10.2;
        merchant.setCustomerRepeatRate(LOW_RETURN_RATE);
        List<Product> products = List.of(product);
        when(productRepository.findByTradingPair(currencyPair))
                .thenReturn(products);

        when(merchantBankInfoRepository.findByMerchantId(merchant))
                .thenReturn(Optional.of(merchantBankInfo));
        NavigableSet<Merchant> result = bureauDeChange.findNearestExchangeMerchant(location, BigDecimal.valueOf(500), currencyPair);
        assertTrue(result.isEmpty());
    }
    @Test
    void testFindNearestExchangePeerInsufficientBalance() {
        NavigableSet<Merchant> result = bureauDeChange.findNearestExchangeMerchant(location, BigDecimal.valueOf(500), currencyPair);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindNearestExchangePeerOutsideRange() {
        Merchant farMerchant = new Merchant("merchant2", 10.0000, 10.0000, PenaltyWeight.SLOW_DELIVERY);
        NavigableSet<Merchant> merchants = new TreeSet<>(Comparator.comparing(Merchant::getPenalty));
        merchants.add(farMerchant);

        NavigableSet<Merchant> result = bureauDeChange.findNearestExchangeMerchant(location, BigDecimal.valueOf(500), currencyPair);
        assertTrue(result.isEmpty());
    }

    @Test
    void testMatchSuccess() {
        Peer requester = new Peer("user1", location, BigDecimal.valueOf(500), currencyPair);
        NavigableSet<Merchant> result = bureauDeChange.match(requester, location, BigDecimal.valueOf(500), currencyPair);
        assertNotNull(result);
    }

    @Test
    void testMatchNoMerchantsAvailable() {
        Peer requester = new Peer("user1", location, BigDecimal.valueOf(500), currencyPair);
        NavigableSet<Merchant> result = bureauDeChange.match(requester, location, BigDecimal.valueOf(500), currencyPair);
        assertTrue(result.isEmpty());
    }

    @Test
    void testHandleExchangeTransaction() {
        ExchangeTransaction exchangeTransaction = new ExchangeTransaction(
                "user1",
                location,
                BigDecimal.valueOf(500),
                currencyPair
        );
        assertDoesNotThrow(() -> bureauDeChange.handleExchangeTransaction(exchangeTransaction));
    }
}

