package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data;

import lombok.Builder;
import lombok.Data;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Usecase;

@Data
@Builder
public class TicketParameter {
    private Long id;
    private Usecase usecase;
    private String msisdn;
    private int question;
}
