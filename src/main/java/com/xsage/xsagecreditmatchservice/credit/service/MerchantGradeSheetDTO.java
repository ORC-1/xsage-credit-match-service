package com.xsage.xsagecreditmatchservice.credit.service;

import lombok.Data;

@Data
public class MerchantGradeSheetDTO {
    private String merchantId;
    private double location;
    private long completion;
    private double timeToComplete;
    private double durationOnPlatform;
    private double merchantCommissionRate;
    private double penalty;
    private int quota;
    private double customerRepeatRate;
    private double transferEnabled;
    private double fulfillmentInstitutionSymmetry;
    private double totalScore;
}
