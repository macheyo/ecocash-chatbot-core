package zw.co.cassavasmartech.ecocashchatbotcore.eip.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EipTransaction {
    private String clientCorrelator;
    private String notifyUrl;
    private String referenceCode;
    private String tranType;
    private String endUserId;
    private String remarks;
    private String transactionOperationStatus;
    private PaymentAmount paymentAmount;
    private String merchantCode;
    private String merchantPin;
    private String merchantNumber;
    private String currencyCode;
    private String countryCode;
    private String terminalID;
    private String location;
    private String ecocashReference;
    private String superMerchantName;
    private String merchantName;
    private String originalServerReferenceCode;
    private String originalEcocashReference;
    private String remark;
    private long endTime;
    private long startTime;
    private String serverReferenceCode;
    private String responseCode;
}
