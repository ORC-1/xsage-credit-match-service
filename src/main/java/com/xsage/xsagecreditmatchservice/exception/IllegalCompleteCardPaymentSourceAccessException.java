package com.xsage.xsagecreditmatchservice.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class IllegalCompleteCardPaymentSourceAccessException extends RuntimeException {
    Integer code;
    String message;
    String description;
}
