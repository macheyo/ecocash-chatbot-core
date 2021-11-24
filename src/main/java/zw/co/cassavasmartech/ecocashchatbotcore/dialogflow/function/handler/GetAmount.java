package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.FunctionAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PromptObject;

import java.util.Map;

public class GetAmount extends FunctionAdapter {
    public boolean hasArgs() { return true; }

    @Override
    public String process(PromptObject... args) {
        String amount="";
        Map<String, Object> ticket = DialogFlowUtil.getTicket(args[0].getWebhookRequest());
        Map<String, Object> recursion = DialogFlowUtil.getRecursion(args[0].getWebhookRequest());
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> payment = objectMapper.convertValue(ticket.get("payment"),Map.class);
        String intent = args[0].getWebhookRequest().getQueryResult().getIntent().getDisplayName();
        if(intent.substring(intent.length() - 9).equalsIgnoreCase("scenario2"))amount=payment.get("amount").toString();
        else amount=ticket.get("amount").toString();
        return amount;
    }
}
