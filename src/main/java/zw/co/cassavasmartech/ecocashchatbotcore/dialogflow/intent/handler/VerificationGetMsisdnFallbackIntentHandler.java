package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler;

import lombok.extern.slf4j.Slf4j;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Usecase;
@Slf4j
public class VerificationGetMsisdnFallbackIntentHandler extends IntentHandlerAdapter {
    @Override
    public WebhookResponse getWebhookResponse(WebhookRequest... webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest[0].getQueryResult().getIntent().getDisplayName());
        Customer customer = new Customer();
        Object[] context = null;
        String prompt = DialogFlowUtil.promptProcessor(3, webhookRequest[0], customer);
        return DialogFlowUtil.getResponse(webhookRequest[0], prompt, context, Usecase.VERIFICATION);
    }
}
