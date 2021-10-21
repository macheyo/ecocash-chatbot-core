package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.sendMoney;

import lombok.extern.slf4j.Slf4j;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UseCase;

@Slf4j
public class SendMoneyConfirmationAffirmativeIntentHandler extends IntentHandlerAdapter {
    @Override
    public WebhookResponse getWebhookResponse(WebhookRequest... webhookRequest) {
        log.info("Processing Dialogflow Intent: {}", webhookRequest[0].getQueryResult().getIntent().getDisplayName());
        Customer customer = DialogFlowUtil.isNewCustomer(webhookRequest[0]);
        String response = DialogFlowUtil.sendMoney(webhookRequest[0]);

        if(response.equalsIgnoreCase("603")){
            return DialogFlowUtil.getResponse(webhookRequest[0],
                    DialogFlowUtil.promptProcessor(4,webhookRequest[0],customer), // push sent
                    new Object[]{},
                    UseCase.SEND_MONEY);
        }else{
            return DialogFlowUtil.getResponse(
                    webhookRequest[0],
                    DialogFlowUtil.promptProcessor(5,webhookRequest[0],customer), //SOMETHING WENT WRONG
                    new Object[]{},
                    UseCase.SEND_MONEY);
        }
    }
}
