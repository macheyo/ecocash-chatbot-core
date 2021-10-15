package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.airtime;

import lombok.extern.slf4j.Slf4j;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.OutputContext;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UseCase;
@Slf4j
public class SendAirtimeGetBeneficiaryMsisdnIntentHandler extends IntentHandlerAdapter {
    @Override
    public WebhookResponse getWebhookResponse(WebhookRequest... webhookRequest) {
        log.info("Processing Dialogflow Intent: {}", webhookRequest[0].getQueryResult().getIntent().getDisplayName());
        Customer customer = DialogFlowUtil.isNewCustomer(webhookRequest[0]);
        if(customer != null){
            if(!DialogFlowUtil.isCustomerValid(webhookRequest[0])){
                OutputContext outputContext = OutputContext.builder()
                        .lifespanCount(1)
                        .name(webhookRequest[0].getSession()+"/contexts/awaiting_beneficiary_msisdn")//TODO put correct context here
                        .build();

                OutputContext contextToRemove = OutputContext.builder()
                        .lifespanCount(0)
                        .name(webhookRequest[0].getSession()+"/contexts/awaiting_airtime_amount")//TODO put correct context here
                        .build();

                return DialogFlowUtil.getResponse(webhookRequest[0],
                        DialogFlowUtil.promptProcessor(7,webhookRequest[0],customer),
                        new Object[]{outputContext,contextToRemove},
                        UseCase.BUY_AIRTIME);
            }
            return DialogFlowUtil.getResponse(webhookRequest[0],
                    DialogFlowUtil.promptProcessor(6,webhookRequest[0],customer),
                    new Object[]{},
                    UseCase.BUY_AIRTIME);
        }
        return DialogFlowUtil.defaultUnknownCustomerResponse(webhookRequest[0]);
    }
}
