package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.handler;

import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.FunctionAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PromptObject;

import java.util.Map;

public class GetBillAccount extends FunctionAdapter {
    public boolean hasArgs() { return true; }

    @Override
    public String process(PromptObject... promptObject) {
        Map<String, Object> ticket = DialogFlowUtil.getTicket(promptObject[0].getWebhookRequest());
        return ticket.get("number.original").toString();
    }
}
