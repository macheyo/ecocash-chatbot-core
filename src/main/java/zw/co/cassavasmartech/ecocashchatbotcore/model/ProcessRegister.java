package zw.co.cassavasmartech.ecocashchatbotcore.model;

import com.econetwireless.common.jpa.conveters.ClassConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;

import javax.persistence.*;

/**
 *
 * @author benard
 */
@Entity
@Table(name = "process_register")
@Audited
@Getter
@Setter
@ToString(exclude = {"notificationHandlerClass","paymentMethod","tranType","messageCorrelation","processFlow","code"})
public class ProcessRegister extends BaseEntity {

    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "process_flow")
    private String processFlow;
    @Column(name = "active", nullable = false)
    private Boolean enabled;
    @Convert(converter = ClassConverter.class)
    @Column(name = "notification_handler_class", length = 200)
    private Class notificationHandlerClass;
    @Basic(optional = false)
    @Column(name = "message_correlation")
    private String messageCorrelation;
    @JoinColumn(name = "payment_method", referencedColumnName = "id")
    @ManyToOne
    private PaymentMethod paymentMethod;
    @JoinColumn(name = "transaction_type")
    @ManyToOne
    private TransactionType tranType;

}
