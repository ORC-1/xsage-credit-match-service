package com.xsage.xsagecreditmatchservice.shared.util;


import com.xsage.xsagecreditmatchservice.shared.RestTemplateClient;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;

@Component
@AllArgsConstructor
public class RestClientCacheableImpl implements RestClientCacheable {
    private final RestTemplateClient restTemplate;
    private final XsageProps xsageProps;

    @Override
    public String getRequestWithAuth(String url) {
        throw new NotImplementedException();
    }

    @Override
    public byte[] getByteRequestWithAuth(String url) {
        return new byte[0];
    }

    @Override
    public String getRequestWithNoAuth(String url) {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
          return response.getBody();
    }

    @Override
    public byte[] getByteRequestWithNoAuth(String url) {
        return new byte[0];
    }

    @Override
    public String postRequestNoAuth(String url, String body) {
        return restTemplate.postForObject(url, body, String.class);
    }

    @Override
    public String postRequestWithAuth(String url, Object reqBody) {
        String accessToken = getSecretKey();
        RestTemplate buildRestTemplate = new RestTemplateBuilder(rt -> rt.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + accessToken);
            return execution.execute(request, body);
        })).build();
        return buildRestTemplate.postForObject(url, reqBody, String.class);
    }


    @Override
    public ResponseEntity<String> postRequestWithAuthReturnsResponseEntity(String key, String url, Object payload) {
        RestTemplate buildRestTemplate = new RestTemplateBuilder(rt -> rt.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + key);
            return execution.execute(request, body);
        })).build();
        return buildRestTemplate.postForEntity(url, payload, String.class);
    }

    public String getSecretKey() {
        switch (ValidPaymentGateWay.valueOf(xsageProps.getACTIVE_PAYMENT_GATEWAY().toUpperCase(Locale.ROOT))) {
            case PAYSTACK:
                return xsageProps.getPAY_STACK_SECRET_KEY();
            default:
                return "";
        }
    }
}
