package zw.co.cassavasmartech.ecocashchatbotcore.model;

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
@Table(name = "payment_method")
@Audited
@Getter
@Setter
@ToString(exclude = {"name","code","tranType"})
public class PaymentMethod extends BaseEntity {

    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "code")
    private String code;
    @JoinColumn(name = "transaction_type")
    @ManyToOne
    private zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionType tranType;

}
