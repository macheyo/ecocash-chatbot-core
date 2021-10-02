package zw.co.cassavasmartech.ecocashchatbotcore.cpg.data;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SubscriberAirtimeRequest {
    private String msisdn1;
    private String msisdn2;
    private BigDecimal amount;
    private Long ticketId;

}
