package zw.co.cassavasmartech.ecocashchatbotcore.payment;

import lombok.Data;
import zw.co.cassavasmartech.ecocashchatbotcore.common.enums.RequestChannel;
import zw.co.cassavasmartech.ecocashchatbotcore.model.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PaymentRequest implements Serializable {
    private String sponsoringMsisdn;
    private String beneficiaryMsisdn;
    private BigDecimal amount;
    private RequestChannel requestChannel;
    private PaymentMethod paymentMethod;
    private Currency currency;
    private String remarks;
    private Long identifier;
    private String receiptNumber;
    private TransactionType transactionType;
    private Partner partner;
    private String transactionReference;
    private String sourceReference;
    private ProcessRegister processRegister;
    private String callBackUrl;
    private String accountNumber;
    private String customerName;
    private String zimraOfficeCode;
    private String taxCode;
    private String customerEmailAddress;
    private String customerData;
    private BigDecimal commision;
    //telone
    private Integer productId;
    //zol
    private String serviceLogin;
    private String serviceName;
    private String servicePwd;
}
