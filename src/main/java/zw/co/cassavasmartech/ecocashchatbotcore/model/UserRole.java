package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Audited
@Entity
@Table(name = "user_role")
@Data
public class UserRole extends BaseEntity {

    private String username;
    @JoinColumn(name = "role", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Role role;


}
