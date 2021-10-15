package zw.co.cassavasmartech.ecocashchatbotcore.payment.cpg.data;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MerchantToMerchantPaymentRequest {

    private String sourceMerchantMsisdn;
    private String destinationMerchantMsisdn;
    private String sourceRef;
    private String sourceMerchantCode;
    private BigDecimal amount;
    private String currency;
    private String merchantPin;

}
