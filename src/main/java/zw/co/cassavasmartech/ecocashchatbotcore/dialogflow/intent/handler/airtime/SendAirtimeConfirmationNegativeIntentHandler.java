package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.airtime;

import lombok.extern.slf4j.Slf4j;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UseCase;
@Slf4j
public class SendAirtimeConfirmationNegativeIntentHandler extends IntentHandlerAdapter {
    @Override
    public WebhookResponse getWebhookResponse(WebhookRequest... webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest[0].getQueryResult().getIntent().getDisplayName());
        return DialogFlowUtil.getResponse(webhookRequest[0],
                DialogFlowUtil.promptProcessor(17,webhookRequest[0],DialogFlowUtil.isNewCustomer(webhookRequest[0])),
                new Object[]{},
                UseCase.BUY_AIRTIME);
    }
}
