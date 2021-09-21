package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler;

import lombok.extern.slf4j.Slf4j;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Usecase;

@Slf4j
public class VerificationGetOTPIntentHandler extends IntentHandlerAdapter {
    @Override
    public WebhookResponse getWebhookResponse(WebhookRequest... webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest[0].getQueryResult().getIntent().getDisplayName());
        if(DialogFlowUtil.verifyCustomer(DialogFlowUtil.getChatId(webhookRequest[0].getOriginalDetectIntentRequest()),webhookRequest[0].getQueryResult().getQueryText())) {
            return DialogFlowUtil.resumeConversation(webhookRequest[0]);
        }
        else {
            String prompt = DialogFlowUtil.promptProcessor(4, webhookRequest[0], new Customer());
            return DialogFlowUtil.getResponse(webhookRequest[0], prompt, null, Usecase.VERIFICATION);
        }
    }
}
