package zw.co.cassavasmartech.ecocashchatbotcore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowService;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;

import javax.validation.Valid;
import java.text.ParseException;

@RestController
@RequestMapping("/dialogflow")
public class DialogFlowController {
    @Autowired
    DialogFlowService dialogFlowService;

    @PostMapping("/inbound")
    WebhookResponse inbound(@Valid @RequestBody WebhookRequest webhookRequest) throws ParseException {
        return dialogFlowService.processWebhookCall(webhookRequest);
    }
}
