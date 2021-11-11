package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.handler;

import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.FunctionAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PromptObject;

import java.util.Map;

public class GetStatementDetails extends FunctionAdapter {
    @Override
    public String process(PromptObject... args) {
        String[] statementDetails = DialogFlowUtil.getStatementConfirmationDetails(args[0].getWebhookRequest());
        return "from "+statementDetails[0]+" to "+statementDetails[1];
    }
}
