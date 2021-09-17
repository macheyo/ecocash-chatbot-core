package zw.co.cassavasmartech.ecocashchatbotcore.functions;

import com.fasterxml.jackson.databind.ObjectMapper;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.model.FunctionAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Platform;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PromptObject;

import java.util.Calendar;
import java.util.Map;

public class GetTimeOfDay extends FunctionAdapter {
    public boolean hasArgs() { return false; }

    @Override
    public String process(PromptObject... args) {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if(timeOfDay >= 0 && timeOfDay < 12){
            return "morning";
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            return "afternoon";
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            return "evening";
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            return "night";
        }
        return "day";
    }
}
