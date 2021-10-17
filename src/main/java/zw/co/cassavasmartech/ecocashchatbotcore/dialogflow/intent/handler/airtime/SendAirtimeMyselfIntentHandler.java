package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.airtime;

import lombok.extern.slf4j.Slf4j;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.OutputContext;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UseCase;

@Slf4j
public class SendAirtimeMyselfIntentHandler extends IntentHandlerAdapter {
    @Override
    public WebhookResponse getWebhookResponse(WebhookRequest... webhookRequest) {
        log.info("Processing Dialogflow Intent: {}", webhookRequest[0].getQueryResult().getIntent().getDisplayName());
        return DialogFlowUtil.getResponse(
                webhookRequest[0],
                DialogFlowUtil.promptProcessor(3,webhookRequest[0],DialogFlowUtil.isNewCustomer(webhookRequest[0])),
                new Object[]{OutputContext.builder()
                        .lifespanCount(1)
                        .name(webhookRequest[0].getSession()+"/contexts/awaiting_send_airtime_amount")
                        .build()},
                UseCase.BUY_AIRTIME
        );
    }
}
