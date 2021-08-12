package zw.co.cassavasmartech.ecocashchatbotcore.cpg.data;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class SubscriberToBillerRequest {
    private BigDecimal amount;
    private String billerCode;
    private String msisdn;
    private String pin;
}
