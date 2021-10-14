package zw.co.cassavasmartech.ecocashchatbotcore.payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import zw.co.cassavasmartech.ecocashchatbotcore.common.enums.TransactionStatus;


import java.io.Serializable;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PaymentResponse implements Serializable {

    @JsonProperty("field1")
    private int responseCode;
    @JsonProperty("field2")
    private String responseMessage;
    @JsonProperty("field3")
    private String paymentReference;
    @JsonProperty("field4")
    private String receiptNumber;
    @JsonProperty("field5")
    private TransactionStatus transactionStatus;
    @JsonIgnore
    private String callBackUrl;
    @JsonProperty("field6")
    private String sourceReference;
    @JsonProperty("field7")
    private String subscriberMsisdn;
    @JsonProperty("field8")
    private String customerName;
    @JsonProperty("field9")
    private String customerData;
    @JsonProperty("field10")
    private String accountNumber;
    @JsonProperty("field11")
    private String currency;
    @JsonProperty("field12")
    private String amount;
    @JsonProperty("field13")
    private String productId;




    public PaymentResponse(String paymentReference) {
        this.paymentReference = paymentReference;
    }


}
