package zw.co.cassavasmartech.ecocashchatbotcore.cpg.data;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SubscriberToMerchantRequest {
    private BigDecimal amount;
    private String merchantMsisdn;
    private String subscriberMsisdn;
    private String pin;
    private Long ticketId;
}
