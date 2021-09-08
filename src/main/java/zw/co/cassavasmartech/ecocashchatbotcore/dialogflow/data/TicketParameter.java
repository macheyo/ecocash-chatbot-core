package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketParameter {
    private Long id;
    private String msisdn;
    private int question;
}
