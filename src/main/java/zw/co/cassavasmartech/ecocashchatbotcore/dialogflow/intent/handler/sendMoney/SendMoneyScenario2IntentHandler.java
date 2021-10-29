package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.sendMoney;

import lombok.extern.slf4j.Slf4j;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UseCase;

@Slf4j
public class SendMoneyScenario2IntentHandler extends IntentHandlerAdapter {
    @Override
    public WebhookResponse getWebhookResponse(WebhookRequest... webhookRequest) {
        log.info("Processing Dialogflow Intent: {}", webhookRequest[0].getQueryResult().getIntent().getDisplayName());
        Customer customer = DialogFlowUtil.isNewCustomer(webhookRequest[0]);
        if(customer!=null)
            return DialogFlowUtil.getResponse(webhookRequest[0],
                DialogFlowUtil.promptProcessor(16,webhookRequest[0],customer),
                DialogFlowUtil.createTicket(webhookRequest[0],UseCase.SEND_MONEY),
                UseCase.SEND_MONEY);
        else return DialogFlowUtil.getResponse(webhookRequest[0],
                DialogFlowUtil.promptProcessor(16,webhookRequest[0],customer),
                new Object[]{},
                UseCase.SEND_MONEY);
    }
}
