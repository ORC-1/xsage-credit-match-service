package com.xsage.xsagecreditmatchservice.exception;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class APIError {
    private List<String> errorMessage;

    private String errorCode;

    private String request;

    private String requestType;

    private String customMessage;
}
