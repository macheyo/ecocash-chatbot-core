package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.verification;

import lombok.extern.slf4j.Slf4j;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.OutputContext;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Usecase;
@Slf4j
public class RegisterConfirmationNegativeIntentHandler extends IntentHandlerAdapter {
    @Override
    public WebhookResponse getWebhookResponse(WebhookRequest... webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest[0].getQueryResult().getIntent().getDisplayName());
        Customer customer = DialogFlowUtil.isNewCustomer(webhookRequest[0]);
        String prompt;
        Object[] context;
        prompt = DialogFlowUtil.promptProcessor(1, webhookRequest[0], customer);
        OutputContext outputContext = OutputContext.builder()
                .lifespanCount(1)
                .name(webhookRequest[0].getSession() + "/contexts/awaiting_registration_confirmation")//TODO put correct context here
                .build();
        context = new Object[]{outputContext};
        return DialogFlowUtil.getResponse(webhookRequest[0],prompt,context, Usecase.VERIFICATION);
    }
}
