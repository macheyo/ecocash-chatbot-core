package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.merchantPayment;

import lombok.extern.slf4j.Slf4j;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UseCase;

@Slf4j
public class PayMerchantScenario1IntentHandler extends IntentHandlerAdapter {
    @Override
    public WebhookResponse getWebhookResponse(WebhookRequest... webhookRequest) {
        log.info("Processing Dialogflow Intent: {}" , webhookRequest[0].getQueryResult().getIntent().getDisplayName());
        Customer customer = DialogFlowUtil.isNewCustomer(webhookRequest[0]);
        String prompt;
        if(customer != null){
            prompt = DialogFlowUtil.promptProcessor(1,webhookRequest[0],customer);  //TO BE mapped to db
        }else{
            prompt = DialogFlowUtil.promptProcessor(2,webhookRequest[0],null); //TO BE mapped to db
        }
        return DialogFlowUtil.getResponse(
                webhookRequest[0],
                prompt,
                DialogFlowUtil.createTicket(webhookRequest[0], UseCase.MERCHANT_PAYMENT),
                UseCase.MERCHANT_PAYMENT);
    }
}
