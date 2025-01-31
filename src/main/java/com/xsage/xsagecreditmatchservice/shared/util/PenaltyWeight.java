package com.xsage.xsagecreditmatchservice.shared.util;

public enum PenaltyWeight {
    SLOW_DELIVERY(3),
    FAILED_TO_DELIVER(7),
    FRAUD_ALLEGATION(10);

    private double enumValue;

    PenaltyWeight(double variableValue) {
        this.enumValue = variableValue;
    }

    public double getEnumValue() {
        return this.enumValue;
    }
}
