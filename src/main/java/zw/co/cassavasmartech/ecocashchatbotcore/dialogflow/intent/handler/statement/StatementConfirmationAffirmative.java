package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.statement;

import lombok.extern.slf4j.Slf4j;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.EipTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UseCase;

@Slf4j
public class StatementConfirmationAffirmative extends IntentHandlerAdapter {
    @Override
    public WebhookResponse getWebhookResponse(WebhookRequest... webhookRequest) {
        log.info("Processing Dialogflow Intent: {}", webhookRequest[0].getQueryResult().getIntent().getDisplayName());
        Customer customer = DialogFlowUtil.isNewCustomer(webhookRequest[0]);
        EipTransaction response = DialogFlowUtil.payMerchant(webhookRequest[0]);
        if(response.getTransactionOperationStatus().equalsIgnoreCase("PENDING SUBSCRIBER VALIDATION"))
       {
                return DialogFlowUtil.getResponse(webhookRequest[0],
                    DialogFlowUtil.promptProcessor(10,webhookRequest[0],customer), // push sent
                    new Object[]{},
                    UseCase.SUBSCRIBER_STATEMENT);
        }else{
            return DialogFlowUtil.getResponse(
                    webhookRequest[0],
                    DialogFlowUtil.promptProcessor(11,webhookRequest[0],customer), //SOMETHING WENT WRONG
                    new Object[]{},
                    UseCase.SUBSCRIBER_STATEMENT);
        }
    }
}
