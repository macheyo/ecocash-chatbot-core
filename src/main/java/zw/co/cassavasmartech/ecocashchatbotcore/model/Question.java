package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Data
public class Question extends BaseEntity {
    private String text;
    private boolean status;
}
