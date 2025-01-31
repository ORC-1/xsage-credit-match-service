package com.xsage.xsagecreditmatchservice.shared.util;

public enum ValidPaymentGateWay {
    PAYSTACK("paystack"),
    STRIPE("stripe");
    private String enumValue;

    ValidPaymentGateWay(String variableName) {
        this.enumValue = variableName;
    }

    public String getEnumValue() {
        return this.enumValue;
    }
}
