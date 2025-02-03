package com.xsage.xsagecreditmatchservice.credit.domain;

import com.xsage.xsagecreditmatchservice.shared.util.ValidCurrencyPair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "merchant_id_id")
    private Merchant merchantId;

    @Enumerated(EnumType.STRING)
    private ValidCurrencyPair tradingPair;

    @NotNull
    private Double merchantInternalRate;

    @NotNull
    private Double sellRate;

    @NotNull
    private double buyRate;


    public Product(Merchant merchant1, ValidCurrencyPair currencyPair, double sellRate, double buyRate) {
        this.merchantId = merchant1;
        this.tradingPair = currencyPair;
        this.sellRate = sellRate;
        this.buyRate = buyRate;
    }
}
