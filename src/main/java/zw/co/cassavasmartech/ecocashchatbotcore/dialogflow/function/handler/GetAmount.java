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
        if(recursion.get("intent").toString().equalsIgnoreCase("usecase_pay_merchant_scenario1"))amount=ticket.get("amount").toString();
        else amount=payment.get("amount").toString();
        return amount;
    }
}
