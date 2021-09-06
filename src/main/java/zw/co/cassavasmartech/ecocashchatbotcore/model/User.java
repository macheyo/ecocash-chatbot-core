package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Audited
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "msisdn", name = "un_users_msisdn"),
        @UniqueConstraint(columnNames = "email", name = "un_users_email")
})
@Data
public class User extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "username")
    private String username;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "enabled")
    private boolean enabled;
    @Column(name = "email")
    private String email;
    @Column(name = "msisdn")
    private String msisdn;
    @Column(name = "first_log_in")
    private boolean firstLogIn = true;
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private zw.co.cassavasmartech.ecocashchatbotcore.model.UserGroup userGroup;

}
