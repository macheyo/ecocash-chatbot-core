package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.merchantPayment;

import lombok.extern.slf4j.Slf4j;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.OutputContext;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UseCase;

@Slf4j
public class PayMerchantGetMsisdnIntentHandler extends IntentHandlerAdapter {
    @Override
    public WebhookResponse getWebhookResponse(WebhookRequest... webhookRequest) {
        log.info("Processing Dialogflow Intent: {}", webhookRequest[0].getQueryResult().getIntent().getDisplayName());
        Customer customer = DialogFlowUtil.isNewCustomer(webhookRequest[0]);
        if (customer != null){
            if(!DialogFlowUtil.isMerchantNameOrCodeValid(webhookRequest[0])){
                OutputContext outputContext = OutputContext.builder()
                        .lifespanCount(1)
                        .name(webhookRequest[0].getSession()+"/contents/awaiting_merchant_msisdn")//TODO create context on DF
                        .build();
                OutputContext contextToRemove = OutputContext.builder()
                        .lifespanCount(0)
                        .name(webhookRequest[0].getSession()+"/context/awaiting_merchant_amount")//TODO create context on DF
                        .build();
                return DialogFlowUtil.getResponse(webhookRequest[0],
                    DialogFlowUtil.promptProcessor(1,webhookRequest[0],customer),
                    new Object[]{outputContext,contextToRemove},
                    UseCase.MERCHANT_PAYMENT);
            }

            return DialogFlowUtil.getResponse(webhookRequest[0],
                    DialogFlowUtil.promptProcessor(2,webhookRequest[0],customer),
                    new Object[]{},
                    UseCase.MERCHANT_PAYMENT);
        }
        else return DialogFlowUtil.defaultUnknownCustomerResponse(webhookRequest[0]);
    }
}
