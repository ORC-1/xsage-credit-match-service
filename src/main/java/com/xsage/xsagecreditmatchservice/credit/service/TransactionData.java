package com.xsage.xsagecreditmatchservice.credit.service;

import com.xsage.xsagecreditmatchservice.shared.util.ValidCurrencyPair;
import com.xsage.xsagecreditmatchservice.shared.util.ValidTransactionStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionData {
    private Long id;
    private BigDecimal amount;
    private ValidCurrencyPair currency;
    private boolean hasTeller;
    private String tellerNumber;
    private String description;
    private String requestCode;
    private ValidTransactionStatus status;
    private boolean paid = false;
    private String offlineReference;
    private String customer;
    private Date createdAt;
    private String adminId;
    private Date manualApprovalDate;
    double targetLatitude;
    double targetLongitude;
    String location;
    String bankName;
}
