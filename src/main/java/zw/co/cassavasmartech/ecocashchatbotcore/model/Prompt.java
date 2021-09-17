package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.Data;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.persistence.Entity;

@Data
@Entity
public class Prompt extends BaseEntity{
    private String intent;
    private Usecase usecase;
    private String description;
    private int stage;
    private String text;
}
