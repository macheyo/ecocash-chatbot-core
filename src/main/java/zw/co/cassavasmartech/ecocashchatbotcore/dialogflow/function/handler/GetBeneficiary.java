package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.handler;

import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.FunctionAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PromptObject;

import java.util.Map;

public class GetBeneficiary extends FunctionAdapter {
    public boolean hasArgs() { return true; }

    @Override
    public String process(PromptObject... args) {
        Map<String, Object> ticket = DialogFlowUtil.getTicket(args[0].getWebhookRequest());
        String[] beneficiary = DialogFlowUtil.getBeneficiaryDetails(ticket.get("msisdn").toString());
        return ticket.get("msisdn").toString()+" ("+ beneficiary[0]+" "+ beneficiary[1] +")";
    }
}
