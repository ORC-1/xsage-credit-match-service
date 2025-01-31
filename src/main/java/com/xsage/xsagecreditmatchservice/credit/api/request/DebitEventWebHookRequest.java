package com.xsage.xsagecreditmatchservice.credit.api.request;

import lombok.Data;
import javax.annotation.Nullable;

@Data
public class DebitEventWebHookRequest {
    public String Type;
    public String MessageId;
    public String TopicArn;
    @Nullable
    public String Token;
    @Nullable
    public String Subject;
    @Nullable
    public String SubscribeURL;
    public String Message;
    public String Timestamp;
    public String SignatureVersion;
    public String Signature;
    public String SigningCertURL;
}
