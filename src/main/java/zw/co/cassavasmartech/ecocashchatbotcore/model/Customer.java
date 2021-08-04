package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Customer extends BaseEntity{
    private String msisdn;
    private String firstName;
    private String lastName;
    private String dob;
    private String natId;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn
    private List<Profile> profiles;
}
