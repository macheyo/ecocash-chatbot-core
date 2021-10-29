package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.merchantPayment;

import lombok.extern.slf4j.Slf4j;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.Merchant;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UseCase;

@Slf4j
public class PayMerchantScenario2IntentHandler extends IntentHandlerAdapter {
    @Override
    public WebhookResponse getWebhookResponse(WebhookRequest... webhookRequest) {
        log.info("Processing Dialogflow intent: {}", webhookRequest[0].getQueryResult().getIntent().getDisplayName());
        Customer customer = DialogFlowUtil.isNewCustomer(webhookRequest[0]);
        if(customer!=null)
            return DialogFlowUtil.getResponse(
                webhookRequest[0],
                DialogFlowUtil.promptProcessor(16, webhookRequest[0],customer),
                DialogFlowUtil.createTicket(webhookRequest[0], UseCase.MERCHANT_PAYMENT),
                UseCase.MERCHANT_PAYMENT
        );
        else return DialogFlowUtil.getResponse(
                webhookRequest[0],
                DialogFlowUtil.promptProcessor(16, webhookRequest[0],customer),
                DialogFlowUtil.createTicket(webhookRequest[0], UseCase.MERCHANT_PAYMENT),
                UseCase.MERCHANT_PAYMENT);
    }
}
