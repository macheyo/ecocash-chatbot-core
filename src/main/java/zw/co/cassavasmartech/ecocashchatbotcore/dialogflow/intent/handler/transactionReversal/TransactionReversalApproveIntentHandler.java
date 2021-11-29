package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.transactionReversal;

import lombok.extern.slf4j.Slf4j;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UseCase;
@Slf4j
public class TransactionReversalApproveIntentHandler extends IntentHandlerAdapter {
    @Override
    public WebhookResponse getWebhookResponse(WebhookRequest... webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest[0].getQueryResult().getIntent().getDisplayName());
        if(DialogFlowUtil.getPendingReversals(webhookRequest[0]).size()>0)
        return DialogFlowUtil.getResponse(webhookRequest[0],
                DialogFlowUtil.promptProcessor(7, webhookRequest[0], DialogFlowUtil.isNewCustomer(webhookRequest[0])),
                DialogFlowUtil.createTicket(webhookRequest[0], UseCase.TRANSACTION_REVERSAL),
                UseCase.TRANSACTION_REVERSAL);
        return DialogFlowUtil.getResponse(webhookRequest[0],
                DialogFlowUtil.promptProcessor(8, webhookRequest[0], DialogFlowUtil.isNewCustomer(webhookRequest[0])),
                new Object[]{},
                UseCase.TRANSACTION_REVERSAL);

    }
}
