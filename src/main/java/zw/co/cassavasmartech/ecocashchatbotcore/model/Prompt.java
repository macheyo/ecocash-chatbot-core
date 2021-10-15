package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.Data;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.Function;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Data
@Entity
public class Prompt extends BaseEntity{
    private String intent;
    private int position;
    private UseCase usecase;
    private String description;
    private String text;
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Function.class)
    private List<Function> functions;
}
