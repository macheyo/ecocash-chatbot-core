package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscriberDto {
    private String msisdn;
    private String secondaryNumber;
    private boolean secondaryNumberVerified;
    private boolean authenticated;
}
