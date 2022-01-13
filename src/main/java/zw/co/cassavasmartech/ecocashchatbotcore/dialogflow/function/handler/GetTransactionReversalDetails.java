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
            return "Got it. So you want to reverse a send money transaction "+transactionDetails[0] + " Done on " + transactionDetails[1] + " to mobile number " + transactionDetails[2] + "(" + transactionDetails[3] + ")" + " of "+transactionDetails[4] +" Can you confirm this is correct";
        }return " I could not find the transaction Id that you entered. do you want to proceed to capture a valid transaction reference so that I can assist you with your reversal.";
    }
}
