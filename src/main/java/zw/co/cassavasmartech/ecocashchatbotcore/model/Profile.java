package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Profile extends BaseEntity{
    private String alias;
    private Platform platform;
    private String chatId;
//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn
//    private List<OTP> otps;
}
