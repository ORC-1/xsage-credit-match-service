package com.xsage.xsagecreditmatchservice.credit.api;

import com.google.gson.Gson;
import com.xsage.xsagecreditmatchservice.api.xsage.base.ApiController;
import com.xsage.xsagecreditmatchservice.credit.api.request.DebitEventWebHookRequest;
import com.xsage.xsagecreditmatchservice.credit.service.DebitEventWebHookHandler;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@ApiController
@RequestMapping(CreditController.PATH)
@AllArgsConstructor
public class CreditController {
    private final DebitEventWebHookHandler debitEventWebHookHandler;
    static final String PATH = "api/merchant/credit/";
    static Gson gson = new Gson();


    @ResponseStatus(HttpStatus.OK)
    @PostMapping(path = "debiteventwebhook", consumes = MediaType.TEXT_PLAIN_VALUE)
    public void DebitEventWebHook(@RequestBody String request) {
        debitEventWebHookHandler.handle(gson.fromJson(request, DebitEventWebHookRequest.class));
    }
}
