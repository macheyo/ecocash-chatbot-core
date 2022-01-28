package zw.co.cassavasmartech.ecocashchatbotcore.eip.data;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
@Data
@Builder
public class SubscriberToMerchantRequest {
    private String msisdn;
    private String merchantMsisdn;
    private String merchantName;
    private String merchantCode;
    private BigDecimal amount;
    private Long ticketId;
}
