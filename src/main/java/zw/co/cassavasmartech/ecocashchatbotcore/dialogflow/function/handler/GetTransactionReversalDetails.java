package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.handler;

import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.FunctionAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PromptObject;

public class GetTransactionReversalDetails extends FunctionAdapter {
    public boolean hasArgs() { return true; }
    @Override
    public String process(PromptObject... args) {
        String[] transactionDetails = DialogFlowUtil.getTransactionDetails(args[0].getWebhookRequest());
        if (transactionDetails != null) {
            return transactionDetails[0] + " Done on " + transactionDetails[1] + " to mobile number " + transactionDetails[2] + "(" + transactionDetails[3] + ")" + " of "+transactionDetails[4];
        }return "Details not found";
    }
}
