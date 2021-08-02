package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class Profile extends BaseEntity{
    private String alias;
    private Platform platform;
    private String chatId;
}
