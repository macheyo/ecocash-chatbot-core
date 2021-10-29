package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.handler.statement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UseCase;

import java.text.ParseException;
@Slf4j
public class StatementScenario2IntentHandler extends IntentHandlerAdapter {
    @Override
    public WebhookResponse getWebhookResponse(WebhookRequest... webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest[0].getQueryResult().getIntent().getDisplayName());
        Customer customer = DialogFlowUtil.isNewCustomer(webhookRequest[0]);
        try {
            DialogFlowUtil.getStatement(webhookRequest[0]);
            return DialogFlowUtil.getResponse(webhookRequest[0],
                    DialogFlowUtil.promptProcessor(4, webhookRequest[0], customer),
                    new Object[]{},
                    UseCase.SUBSCRIBER_STATEMENT);
        } catch (ParseException e) {
            return DialogFlowUtil.getResponse(webhookRequest[0],
                    DialogFlowUtil.promptProcessor(5, webhookRequest[0], customer),
                    new Object[]{},
                    UseCase.SUBSCRIBER_STATEMENT);
        }

    }
}
