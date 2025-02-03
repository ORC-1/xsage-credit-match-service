package com.xsage.xsagecreditmatchservice.credit.service;

import com.google.gson.Gson;
import com.xsage.xsagecreditmatchservice.bdcmatcher.service.BureauDeChange;
import com.xsage.xsagecreditmatchservice.credit.api.request.DebitEventWebHookRequest;
import com.xsage.xsagecreditmatchservice.credit.api.request.ValidSNSMessageType;
import com.xsage.xsagecreditmatchservice.shared.util.RestClientCacheable;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DebitEventWebHookHandler {
    private final RestClientCacheable restClientCacheable;
    private final BureauDeChange bureauDeChange;

    public void handle(DebitEventWebHookRequest request) {
        if (request.getType().equalsIgnoreCase(ValidSNSMessageType.SUBSCRIPTIONCONFIRMATION.toString())) {
            verifyServerAuthenticityOrThrow();
            restClientCacheable.getRequestWithNoAuth(request.SubscribeURL);
            return;
        }
        if (request.getType().equalsIgnoreCase(ValidSNSMessageType.NOTIFICATION.toString())) {
            Gson gson = new Gson();
            bureauDeChange.handleExchangeTransaction(gson.fromJson(request.getMessage(), ExchangeTransaction.class));
        }
    }

    private void verifyServerAuthenticityOrThrow() {
        //Todo: implement https://docs.aws.amazon.com/sns/latest/dg/sns-verify-signature-of-message.html

    }
}
