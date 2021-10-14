package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author benard
 */
@Entity
@Table(name = "currency")
@Audited
@Getter
@Setter
@ToString(exclude = {"standardCode","description"})
public class Currency extends BaseEntity {

    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Column(name = "description")
    private String description;
    @Column(name = "standard_code")
    private String standardCode;

}
