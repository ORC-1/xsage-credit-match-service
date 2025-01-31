package com.xsage.xsagecreditmatchservice.shared.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class XsageProps {
    @Value("${paystack.test.public.key}")
    private String PAY_STACK_PUBLIC_KEY;
    @Value("${paystack.test.secret.key}")
    private String PAY_STACK_SECRET_KEY;
    @Value("${paystack.init.url}")
    private String PAY_STACK_INIT_URL;
    @Value("${payment.active.gateway}")
    private String ACTIVE_PAYMENT_GATEWAY;
    @Value("${queueService.url}")
    private String QUE_SERVICE_URL;
    @Value("${queueService.secret.key}")
    private String QUE_SERVICE_KEY;
    @Value("${queueService.max.retry}")
    private Integer QUE_SERVICE_MAX_RETRY;
}
