package com.xsage.xsagecreditmatchservice.credit.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@ToString
public class MerchantBankInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "merchant_id_id")
    private Merchant merchantId;

    @NotNull
    private String bankName;

    @NotNull
    private String bankAccountNumber;

    @NotNull
    private String bankAccountCurrencyDenomination;
}
