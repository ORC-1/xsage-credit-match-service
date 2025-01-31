package com.xsage.xsagecreditmatchservice.shared.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.xsage.xsagecreditmatchservice.exception.IllegalCompleteCardPaymentSourceAccessException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.util.Locale;

@AllArgsConstructor
@Component
public class ValidateCompleteCardPaymentSource {
    private final XsageProps xsageProps;
    static Gson gson = new Gson();

    public void isSourceValidOrThrow(Object payload, String signature) {
        switch (ValidPaymentGateWay.valueOf(xsageProps.getACTIVE_PAYMENT_GATEWAY().toUpperCase(Locale.ROOT))) {
            case PAYSTACK:
                String key = xsageProps.getPAY_STACK_SECRET_KEY();

                String rawJson = String.valueOf(payload);
                JsonElement body = gson.fromJson(rawJson, JsonElement.class);
                String result = "";
                String HMAC_SHA512 = "HmacSHA512";
                try {
                    byte[] byteKey = key.getBytes("UTF-8");
                    SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA512);
                    Mac sha512_HMAC = Mac.getInstance(HMAC_SHA512);
                    sha512_HMAC.init(keySpec);
                    byte[] mac_data = sha512_HMAC.
                            doFinal(body.toString().getBytes("UTF-8"));
                    result = DatatypeConverter.printHexBinary(mac_data);

                } catch (Exception e) {
                    throw new RuntimeException("Something went wrong " + e.getMessage());
                }
                if (!result.toLowerCase().equals(signature)) {
                    throw new IllegalCompleteCardPaymentSourceAccessException(HttpStatus.FORBIDDEN.value(),
                            "Complete Card Source could not be verified",
                            "Complete Card Source could not be verified");
                }
            default:
                throw new IllegalArgumentException("Invalid Payment GateWay Name");
        }
    }
}
