package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.Data;

@Data
public class SubscriberDto {
    private String msisdn;
    private String secondaryNumber;
    private boolean secondaryNumberVerified;
    private boolean authenticated;
}
