package zw.co.cassavasmartech.ecocashchatbotcore.payment.cpo.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;


@ToString
@Getter
@Setter
public class CardPaymentResponse  implements Serializable {

    private String orderNumber;
    private String cassavaPayId;
    private String paymentReference;
    private String paymentResponseCode;
    private String paymentResponseExplanation;
    private String paymentMobile;
    private String paymentChannel;
    private String paymentServiceProvider;
    private String paymentDate;
    @JsonProperty("status")
    private String paymentStatus;
    private String transactionType;
    private String accountNumber;
    private String country;
    private BigDecimal amount;
    private String currency;
}
