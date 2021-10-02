package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.verification;

import lombok.extern.slf4j.Slf4j;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UseCase;

@Slf4j
public class VerificationGetOTPIntentHandler extends IntentHandlerAdapter {
    @Override
    public WebhookResponse getWebhookResponse(WebhookRequest... webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest[0].getQueryResult().getIntent().getDisplayName());
        if(DialogFlowUtil.verifyCustomer(DialogFlowUtil.getChatId(webhookRequest[0].getOriginalDetectIntentRequest()),webhookRequest[0].getQueryResult().getQueryText())) {
            WebhookResponse response = DialogFlowUtil.resumeConversation(webhookRequest[0]);
            log.info("Processing dialogflow response: {}", response);
            return response;
        }
        else {
            return DialogFlowUtil.getResponse(webhookRequest[0],
                    DialogFlowUtil.promptProcessor(5, webhookRequest[0], new Customer()),
                    new Object[]{},
                    UseCase.VERIFICATION);
        }
    }
}
