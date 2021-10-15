package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data;

import lombok.Builder;
import lombok.Data;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UseCase;

@Data
@Builder
public class TicketParameter {
    private Long id;
    private UseCase usecase;
    private String msisdn;
    private int question;
}
