package zw.co.cassavasmartech.ecocashchatbotcore.cpg.data;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MerchantToMerchantRequest {

    private String sourceMerchantMsisdn;
    private String destinationMsisdn;
    private String sourceRef;
    private String sourceMerchantCode;
    private String sourceMerchantPin;
    private BigDecimal amount;
    private String currency;

}
