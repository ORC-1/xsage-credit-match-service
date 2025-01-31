package com.xsage.xsagecreditmatchservice.credit.service;

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
            String result = restClientCacheable.getRequestWithNoAuth(request.SubscribeURL);
//            Todo: delete printOut
            System.out.println(result);
            return;
        }
        if (request.getType().equalsIgnoreCase(ValidSNSMessageType.NOTIFICATION.toString())) {
            bureauDeChange.handleExchangeTransaction(request.getMessage());
        }
    }

    private void verifyServerAuthenticityOrThrow() {
//        Todo: implement https://docs.aws.amazon.com/sns/latest/dg/sns-verify-signature-of-message.html

    }
}
