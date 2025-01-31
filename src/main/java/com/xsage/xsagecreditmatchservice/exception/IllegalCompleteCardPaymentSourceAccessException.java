package com.xsage.xsagecreditmatchservice.exception;

import lombok.Value;

@Value
public class IllegalCompleteCardPaymentSourceAccessException extends RuntimeException{
    private Integer code;
    private String message;
    private String description;
}
