package zw.co.cassavasmartech.ecocashchatbotcore.cpg.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscriberToSubscriberRequest {

    private String msisdn1;
    private String msisdn2;
    private String amount;
    private Long ticketId;


}
