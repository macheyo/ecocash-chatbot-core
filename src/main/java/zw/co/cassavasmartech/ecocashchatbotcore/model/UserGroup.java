package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.envers.Audited;
import zw.co.cassavasmartech.ecocashchatbotcore.common.Status;

import javax.persistence.*;
import java.util.Set;
import java.util.TreeSet;

@Audited
@Entity
@Table(name = "user_group")
@AllArgsConstructor
@Data
public class UserGroup extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Enumerated(EnumType.STRING)
    @Basic(optional = false)
    @Column(name = "status",columnDefinition = "varchar(50) default 'ACTIVE'")
    private Status status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "group_role_mapping",
            joinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<Role> roles = new TreeSet<>();

    public UserGroup(Long id) {
        this.setId(id);
    }

    public UserGroup() {
    }

}
