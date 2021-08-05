package zw.co.cassavasmartech.ecocashchatbotcore.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
public class Answer{
    private String msisdn;
    private String answer;
    private boolean active;
    private AnswerStatus status;
    private LocalDateTime statusDate;
    private Question question;
}
