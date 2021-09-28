package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.billPayment;

import lombok.extern.slf4j.Slf4j;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.OutputContext;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Usecase;

@Slf4j
public class PayBillGetBillCodeIntentHandler extends IntentHandlerAdapter {
    @Override
    public WebhookResponse getWebhookResponse(WebhookRequest... webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest[0].getQueryResult().getIntent().getDisplayName());
        Customer customer = DialogFlowUtil.isNewCustomer(webhookRequest[0]);
        if(customer!=null) {
            if (!DialogFlowUtil.isBillerCodeValid(webhookRequest[0])) {
                OutputContext outputContext = OutputContext.builder()
                        .lifespanCount(1)
                        .name(webhookRequest[0].getSession() + "/contexts/awaiting_biller_code")
                        .build();
                OutputContext contextToRemove = OutputContext.builder()
                        .lifespanCount(0)
                        .name(webhookRequest[0].getSession() + "/contexts/awaiting_biller_account")
                        .build();
                return DialogFlowUtil.getResponse(webhookRequest[0],
                        DialogFlowUtil.promptProcessor(1, webhookRequest[0], null),
                        new Object[]{outputContext,contextToRemove},
                        Usecase.BILL_PAYMENT);
            }
            return DialogFlowUtil.getResponse(webhookRequest[0],
                    DialogFlowUtil.promptProcessor(2, webhookRequest[0], null),
                    new Object[]{},
                    Usecase.BILL_PAYMENT);
        }
        else return DialogFlowUtil.defaultUnknownCustomerResponse(webhookRequest[0]);

    }
}
