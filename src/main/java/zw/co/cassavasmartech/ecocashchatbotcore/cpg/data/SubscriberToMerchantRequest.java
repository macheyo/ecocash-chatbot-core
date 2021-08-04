package zw.co.cassavasmartech.ecocashchatbotcore.cpg.data;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubscriberToMerchantRequest {
    private String currency;
    private BigDecimal amount;
    private String merchantMsisdn;
    private String subscriberMsisdn;
    private String pin;
    private String sourceRef;
}
