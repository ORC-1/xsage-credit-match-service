package com.xsage.xsagecreditmatchservice.shared.validation.error;

public interface Violation {

    String getField();

    String getCode();

    String getMessage();

}
