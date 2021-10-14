package zw.co.cassavasmartech.ecocashchatbotcore.payment.ecocash.eip.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString(exclude = {"referenceCode","merchantNumber","merchantPin","countryCode","merchantName","terminalID","paymentAmount","location","superMerchantName","originalServerReferenceCode"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AmountTransaction implements Serializable {

    @JsonProperty("clientCorrelator")
    private String clientCorrelator;

    @JsonProperty("endUserId")
    private String endUserId;

    @JsonProperty("transactionOperationStatus")
    private String transactionOperationStatus;

    @JsonProperty("referenceCode")
    private String referenceCode;
    private String notifyUrl;
    private String merchantCode;
    private String merchantNumber;
    private String merchantPin;
    private String ecocashReference;
    private PaymentAmount paymentAmount;
    private String messageId;
    private String text;
    private String remarks;
    private String tranType;
    private String merchantName;
    private String countryCode;
    private String terminalID;
    private String location;
    private String superMerchantName;
    private String originalServerReferenceCode;
    private String originalEcocashReference;
    private String currencyCode;
}
