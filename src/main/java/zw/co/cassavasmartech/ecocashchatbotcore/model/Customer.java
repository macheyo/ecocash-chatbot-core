package zw.co.cassavasmartech.ecocashchatbotcore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Data
public class Customer extends BaseEntity{
    @Column(unique = true)
    private String msisdn;
    private String firstName;
    private String lastName;
    private String dob;
    private String natId;
    @OneToMany(mappedBy = "customer")
    @JsonBackReference
    private Set<Profile> profiles;

}
