package com.xsage.xsagecreditmatchservice.credit.api.request;

import lombok.Data;
import javax.annotation.Nullable;

@Data
@SuppressWarnings("checkstyle:MemberName") //Member Name not conforming to
// Standard Formatting to make it easier to use in request response class conversion Reflection
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
