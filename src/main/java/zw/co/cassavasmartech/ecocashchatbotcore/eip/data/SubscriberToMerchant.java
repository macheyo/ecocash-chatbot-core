package zw.co.cassavasmartech.ecocashchatbotcore.eip.data;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
@Data
public class SubscriberToMerchant {
    private String msisdn;
    private String merchantName;
    private String merchantCode;
    private BigDecimal amount;
    private Long ticketId;
}
