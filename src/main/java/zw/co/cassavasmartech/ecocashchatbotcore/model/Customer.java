package zw.co.cassavasmartech.ecocashchatbotcore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Customer extends BaseEntity{
    @Column(unique = true)
    private String msisdn;
    private String firstName;
    private String lastName;
    private String dob;
    @Column(unique = true)
    private String natId;
    @OneToMany(mappedBy = "customer")
    @JsonBackReference
    private Set<Profile> profiles;
}
