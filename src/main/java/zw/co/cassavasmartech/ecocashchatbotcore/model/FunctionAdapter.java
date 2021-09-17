package zw.co.cassavasmartech.ecocashchatbotcore.model;

import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;

public abstract class FunctionAdapter {
    public boolean hasArgs() { return true; }
    public abstract String process(PromptObject... promptObject);
}
