package com.xsage.xsagecreditmatchservice.shared.util;

import org.springframework.http.ResponseEntity;

public interface RestClientCacheable {


    String getRequestWithAuth(final String url);


    byte[] getByteRequestWithAuth(final String url);


    String getRequestWithNoAuth(final String url);

    byte[] getByteRequestWithNoAuth(final String url);

    String postRequestNoAuth(final String url, final String body);

    String postRequestWithAuth(final String url, final Object body);

    ResponseEntity<String> postRequestWithAuthReturnsResponseEntity(final String key, final String url, final Object body);

}
