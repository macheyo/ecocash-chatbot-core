package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function;

import zw.co.cassavasmartech.ecocashchatbotcore.model.PromptObject;

public abstract class FunctionAdapter {
    public boolean hasArgs() { return true; }
    public abstract String process(PromptObject... promptObject);
}
