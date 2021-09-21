package zw.co.cassavasmartech.ecocashchatbotcore.model;

import lombok.Builder;
import lombok.Data;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;

import java.util.Optional;

@Data
@Builder
public class PromptObject {
    private WebhookRequest webhookRequest;
    private Customer customer;
}
