package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.Intent;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;

import java.text.ParseException;

@Service
@Slf4j
public class DialogFlowServiceImpl implements DialogFlowService {

    @Override
    public WebhookResponse processWebhookCall(WebhookRequest webhookRequest) throws ParseException {
        log.info("Processing dialogflow transaction request {}\n", webhookRequest);
        IntentHandlerAdapter intentHandlerAdapter = Intent.lookup(webhookRequest.getQueryResult().getIntent().getDisplayName());
            if(intentHandlerAdapter!=null)
                return intentHandlerAdapter.getWebhookResponse(webhookRequest);
            else return null;
    }
}