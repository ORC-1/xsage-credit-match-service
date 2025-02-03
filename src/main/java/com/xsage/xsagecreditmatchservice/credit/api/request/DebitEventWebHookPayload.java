package com.xsage.xsagecreditmatchservice.credit.api.request;

import lombok.Value;

import java.io.Serializable;

@Value
public class DebitEventWebHookPayload implements Serializable {
    private static final long serialVersionUID = 1L;
    String source;
    Object message;
}
