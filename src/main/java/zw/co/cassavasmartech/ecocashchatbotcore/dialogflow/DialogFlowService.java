package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow;

import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;

@Service
public interface DialogFlowService {
    WebhookResponse processWebhookCall(WebhookRequest webhookRequest);
}
