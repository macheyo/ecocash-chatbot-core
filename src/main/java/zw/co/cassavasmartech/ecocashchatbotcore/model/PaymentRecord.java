package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.Data;
import org.hibernate.envers.Audited;
import zw.co.cassavasmartech.ecocashchatbotcore.common.enums.PaymentType;
import zw.co.cassavasmartech.ecocashchatbotcore.common.enums.TransactionStatus;

import javax.persistence.*;

@Entity
@Table(name = "payment_record")
@Data
@Audited
public class PaymentRecord extends BaseEntity{
    @Column(name = "response_code")
    private String responseCode;
    @Column(name = "response_message")
    private String responseMessage;
    @Column(name = "payment_status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;
    @JoinColumn(name = "txn", referencedColumnName = "payment_reference")
    @OneToOne
    private Transaction transaction;
    @Column(name = "response_reference")
    private String responseReference;
    @Column(name = "response_data")
    private String responseData;
    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    @JoinColumn(name = "reversal_record", referencedColumnName = "id")
    @OneToOne
    private zw.co.cassavasmartech.ecocashchatbotcore.model.PaymentRecord reversalRecord;
    @Column(name = "request_reference")
    private String requestReference;
    @JoinColumn(name = "transaction_type", referencedColumnName = "id")
    @ManyToOne
    private TransactionType transactionType;
}
