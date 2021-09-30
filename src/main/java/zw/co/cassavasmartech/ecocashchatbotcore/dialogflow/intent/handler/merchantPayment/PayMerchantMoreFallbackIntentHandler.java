package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.merchantPayment;

import lombok.extern.slf4j.Slf4j;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Usecase;

@Slf4j
public class PayMerchantMoreFallbackIntentHandler extends IntentHandlerAdapter {
    @Override
    public WebhookResponse getWebhookResponse(WebhookRequest... webhookRequest) {
        log.info("Processing Dialogflow Intent: {}", webhookRequest[0].getQueryResult().getIntent().getDisplayName());
        Customer customer = DialogFlowUtil.isNewCustomer(webhookRequest[0]);

        return DialogFlowUtil.getResponse(webhookRequest[0],
                DialogFlowUtil.promptProcessor(1,webhookRequest[0],customer),
                new Object[]{},
                Usecase.MERCHANT_PAYMENT

        );
    }
}
