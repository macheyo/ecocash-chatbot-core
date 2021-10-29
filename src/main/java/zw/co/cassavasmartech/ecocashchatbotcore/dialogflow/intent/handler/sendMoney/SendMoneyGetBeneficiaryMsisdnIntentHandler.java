package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.sendMoney;

import lombok.extern.slf4j.Slf4j;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.OutputContext;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UseCase;

@Slf4j
public class SendMoneyGetBeneficiaryMsisdnIntentHandler extends IntentHandlerAdapter {
    @Override
    public WebhookResponse getWebhookResponse(WebhookRequest... webhookRequest) {
       log.info("Processing Dialogflow Intent: {}", webhookRequest[0].getQueryResult().getIntent().getDisplayName());
        Customer customer = DialogFlowUtil.isNewCustomer(webhookRequest[0]);
        if(customer != null){
            if(!DialogFlowUtil.isCustomerValid(webhookRequest[0])){
                OutputContext outputContext = OutputContext.builder()
                        .lifespanCount(1)
                        .name(webhookRequest[0].getSession()+"/contexts/awaiting_send_money_msisdn")
                        .build();

                OutputContext contentToRemove = OutputContext.builder()
                        .lifespanCount(0)
                        .name(webhookRequest[0].getSession()+"/contexts/awaiting_send_money_amount")
                        .build();

                return DialogFlowUtil.getResponse(webhookRequest[0],
                        DialogFlowUtil.promptProcessor(3,webhookRequest[0],customer), //msisdn not valid
                        new Object[]{outputContext, contentToRemove},
                        UseCase.SEND_MONEY);
            }
            return DialogFlowUtil.getResponse(webhookRequest[0],
                    DialogFlowUtil.promptProcessor(2,webhookRequest[0],customer), // msisdn valid
                    new Object[]{},
                    UseCase.SEND_MONEY);

        }
        return DialogFlowUtil.defaultUnknownCustomerResponse(webhookRequest[0]);
    }
}
