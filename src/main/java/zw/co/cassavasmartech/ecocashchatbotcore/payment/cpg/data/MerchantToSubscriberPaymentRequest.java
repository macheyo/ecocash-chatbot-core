package zw.co.cassavasmartech.ecocashchatbotcore.payment.cpg.data;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MerchantToSubscriberPaymentRequest {
    private String merchantMsisdn;
    private String subscriberMsisdn;
    private String sourceRef;
    private String sourceMerchantCode;
    private String merchantPin;
    private BigDecimal amount;
    private String currency;

}
