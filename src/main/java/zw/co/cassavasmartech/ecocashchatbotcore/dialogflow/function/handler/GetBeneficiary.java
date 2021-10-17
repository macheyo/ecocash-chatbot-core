package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.handler;

import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.FunctionAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PromptObject;

import java.util.Map;

public class GetBeneficiary extends FunctionAdapter {
    public boolean hasArgs() { return true; }

    @Override
    public String process(PromptObject... args) {
        String[] beneficiary = DialogFlowUtil.getBeneficiaryDetails(args[0].getWebhookRequest());
        return beneficiary[0]+" ("+ beneficiary[1]+" "+ beneficiary[2] +")";
    }
}
