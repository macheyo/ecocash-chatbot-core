package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent;

import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;

public abstract class IntentHandlerAdapter {
    public abstract WebhookResponse getWebhookResponse(WebhookRequest... webhookRequest);
}
