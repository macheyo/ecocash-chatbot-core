package zw.co.cassavasmartech.ecocashchatbotcore.model;

import com.econetwireless.common.jpa.conveters.ClassConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Audited
@Entity
@Table(name = "transaction_type")
@Getter
@Setter
@ToString(exclude = {"responseHandlerClass","requestHandlerClass"})
public class TransactionType extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Convert(converter = ClassConverter.class)
    @Column(name = "response_handler_class", length = 200)
    private Class responseHandlerClass;

    @Convert(converter = ClassConverter.class)
    @Column(name = "request_handler_class", length = 200)
    private Class requestHandlerClass;

    @JoinColumn(name = "reversal_tran_type", referencedColumnName = "id")
    @OneToOne
    private zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionType reversalTranType;

}