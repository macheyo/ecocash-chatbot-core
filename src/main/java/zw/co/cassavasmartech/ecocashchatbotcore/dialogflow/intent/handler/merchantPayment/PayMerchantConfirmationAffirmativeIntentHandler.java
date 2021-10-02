package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.merchantPayment;

import lombok.extern.slf4j.Slf4j;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.EipTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UseCase;

@Slf4j
public class PayMerchantConfirmationAffirmativeIntentHandler extends IntentHandlerAdapter {
    @Override
    public WebhookResponse getWebhookResponse(WebhookRequest... webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest[0].getQueryResult().getIntent().getDisplayName());
        Customer customer = DialogFlowUtil.isNewCustomer(webhookRequest[0]);
        EipTransaction response = DialogFlowUtil.payMerchant(webhookRequest[0]);
        if(response!=null) return DialogFlowUtil.getResponse(webhookRequest[0],
                DialogFlowUtil.promptProcessor(7,webhookRequest[0],customer),
                new Object[]{},
                UseCase.MERCHANT_PAYMENT);
        else return DialogFlowUtil.getResponse(webhookRequest[0],
                DialogFlowUtil.promptProcessor(8,webhookRequest[0],customer),
                new Object[]{},
                UseCase.MERCHANT_PAYMENT);
    }

}
