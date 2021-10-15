package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.handler;

import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.FunctionAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PromptObject;

import java.util.Map;

public class GetMerchant extends FunctionAdapter {
    @Override
    public String process(PromptObject... args) {
        Map<String, Object> ticket = DialogFlowUtil.getTicket(args[0].getWebhookRequest());
        String[] merchantDetails = DialogFlowUtil.getMerchantDetails(ticket.get("msisdn.original").toString());
        return merchantDetails[1] +" ("+merchantDetails[0]+")";
    }
}
