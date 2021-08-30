package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WebhookResponse {
    private String fulfillmentText;
    private String source;
    private List<OutputContext> outputContexts;
}
