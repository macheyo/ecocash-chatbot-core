package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;
import zw.co.cassavasmartech.ecocashchatbotcore.common.enums.RequestChannel;
import zw.co.cassavasmartech.ecocashchatbotcore.common.enums.TransactionStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author benard
 */
@Entity
@Table(name = "transaction")
@Getter
@Setter
@Audited
@ToString(exclude = {"paymentMethod","partner","processRegister"})
public class Transaction extends BaseEntity implements Serializable {

    @Basic(optional = false)
    @Column(name = "source_reference")
    private String sourceReference;

    @Column(name = "receipt_number")
    private String receiptNumber;

    @Column(name = "payment_reference")
    private String paymentReference;

    @Column(name = "msisdn1")
    private String msisdn1;

    @Column(name = "msisdn2")
    private String msisdn2;

    @Column(name = "merchant_msisdn1")
    private String merchantMsisdn1;

    @Column(name = "merchant_msisdn2")
    private String merchantMsisdn2;

    @Column(name = "amount")
    private BigDecimal amount;

    @Basic(optional = false)
    @Column(name = "tran_status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus tranStatus;

    @JoinColumn(name = "currency", referencedColumnName = "id")
    @ManyToOne
    private Currency currency;

    @JoinColumn(name = "partner", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Partner partner;

    @JoinColumn(name = "payment_method", referencedColumnName = "id")
    @ManyToOne
    private PaymentMethod paymentMethod;

    @JoinColumn(name = "process", referencedColumnName = "id")
    @ManyToOne
    private ProcessRegister processRegister;

    @Column(name = "call_back_url")
    private String callBackUrl;

    @Column(name = "channel")
    @Enumerated(EnumType.STRING)
    private RequestChannel channel;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "status_reason")
    private String statusReason;

    @Column(name = "is_reversed",nullable = false,columnDefinition = "boolean default false")
    private boolean reversed;
    @Column(name = "requires_manual_reversal",nullable = false,columnDefinition = "boolean default false")
    private boolean manualReversal;

    @Column(name = "service_credited",nullable = false,columnDefinition = "boolean default false")
    private boolean serviceCredited;

    @Column(name = "account_number")
    private String account_number;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "tax_code")
    private String taxCode;

    @Column(name = "customer_email_address")
    private String customerEmailAddress;

    @Column(name="customer_data")
    private String customerData;

}
