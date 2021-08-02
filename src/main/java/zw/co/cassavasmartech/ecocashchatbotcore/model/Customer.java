package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Customer extends BaseEntity{
    private String msisdn;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn
    private List<Profile> profiles;
}
