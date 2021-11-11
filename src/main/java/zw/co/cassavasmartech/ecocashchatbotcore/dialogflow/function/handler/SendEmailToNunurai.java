package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.handler;

import org.springframework.beans.factory.annotation.Value;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.DialogFlowUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.FunctionAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.email.data.EmailNotification;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PromptObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class SendEmailToNunurai extends FunctionAdapter {


    public boolean hasArgs() { return true; }
    @Override
    public String process(PromptObject... args) {

        DialogFlowUtil.sendEmail(args[0].getWebhookRequest());
        return "";
    }
}
