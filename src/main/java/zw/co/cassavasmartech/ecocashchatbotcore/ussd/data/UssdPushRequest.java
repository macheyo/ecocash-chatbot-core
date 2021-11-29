package zw.co.cassavasmartech.ecocashchatbotcore.ussd.data;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UssdPushRequest {
    private String status;
    private String callbackUrl;
    private String msisdn;
    private PushType pushType;
    private String reference;
    private Date requestDate;
    private String serviceCode;
    private String message;
}
