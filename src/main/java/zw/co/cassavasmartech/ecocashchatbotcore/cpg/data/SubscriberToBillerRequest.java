package zw.co.cassavasmartech.ecocashchatbotcore.cpg.data;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class SubscriberToBillerRequest {
    private BigDecimal amount;
    private String billerCode;
    private String msisdn;
    private String msisdn2;
    private String pin;
    private Long ticketId;
}
