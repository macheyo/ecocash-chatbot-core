package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data;

import lombok.Data;

@Data
public class WebhookRequest {
    private String responseId;
    private QueryResult queryResult;
    private OriginalDetectIntentRequest originalDetectIntentRequest;
    private String session;

}
