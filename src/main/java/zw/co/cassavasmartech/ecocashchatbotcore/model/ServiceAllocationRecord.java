package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.Data;
import org.hibernate.envers.Audited;
import zw.co.cassavasmartech.ecocashchatbotcore.common.enums.ServiceType;
import zw.co.cassavasmartech.ecocashchatbotcore.common.enums.TransactionStatus;

import javax.persistence.*;

@Entity
@Table(name = "service_allocation_record")
@Data
@Audited
public class ServiceAllocationRecord extends BaseEntity{

    @Column(name = "response_code")
    private String responseCode;
    @Column(name = "response_message")
    private String responseMessage;
    @Column(name = "service_allocation_status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;
    @JoinColumn(name = "txn", referencedColumnName = "payment_reference")
    @OneToOne
    private Transaction transaction;
    @Column(name = "response_reference")
    private String responseReference;
    @Column(name = "response_data")
    private String responseData;
    @Column(name = "service_type")
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;
    @Column(name = "request_reference")
    private String requestReference;

}
