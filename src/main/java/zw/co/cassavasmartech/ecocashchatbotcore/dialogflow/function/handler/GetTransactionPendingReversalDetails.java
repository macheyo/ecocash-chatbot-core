package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.handler;

import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.FunctionAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PromptObject;

public class GetTransactionPendingReversalDetails extends FunctionAdapter {
    public boolean hasArgs() { return true; }
    @Override
    public String process(PromptObject... args) {
        String[] transactionDetails = DialogFlowUtil.getPendingTransactionDetails(args[0].getWebhookRequest());
        if (transactionDetails != null) {
            return transactionDetails[0] + " money received from mobile number " + transactionDetails[1] + "(" + transactionDetails[2] + ")" + " of $ZWL"+transactionDetails[3];
        }return "Details not found";
    }
}
