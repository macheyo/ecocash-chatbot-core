package zw.co.cassavasmartech.ecocashchatbotcore.cpg.data;

import lombok.Data;

@Data
public class SubscriberToSubscriberRequest {

    private String msisdn1;
    private String msisdn2;
    private String amount;
    private Long ticketId;


}
