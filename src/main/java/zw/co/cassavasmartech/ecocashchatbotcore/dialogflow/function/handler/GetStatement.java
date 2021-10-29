package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.handler;

import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.FunctionAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PromptObject;

public class GetStatement extends FunctionAdapter {
    public boolean hasArgs() { return true; }
    @Override
    public String process(PromptObject... promptObject) {
        return null;
    }
}
