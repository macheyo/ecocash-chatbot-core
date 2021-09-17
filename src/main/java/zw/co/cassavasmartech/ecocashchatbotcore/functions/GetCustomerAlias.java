package zw.co.cassavasmartech.ecocashchatbotcore.functions;

import com.fasterxml.jackson.databind.ObjectMapper;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.OriginalDetectIntentRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.model.FunctionAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Platform;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PromptObject;

import java.util.Map;

public class GetCustomerAlias extends FunctionAdapter{
    public boolean hasArgs() { return true; }

    @Override
    public String process(PromptObject... args) {
        if (getPlatform(args[0].getWebhookRequest().getOriginalDetectIntentRequest()).equals(Platform.TELEGRAM)) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = objectMapper.convertValue(args[0].getWebhookRequest().getOriginalDetectIntentRequest().getPayload(), Map.class);
            Map<String, Object> data = objectMapper.convertValue(map.get("data"), Map.class);
            Map<String, Object> from = objectMapper.convertValue(data.get("from"), Map.class);
            return from.get("first_name").toString();
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = objectMapper.convertValue(args[0].getWebhookRequest().getOriginalDetectIntentRequest().getPayload(), Map.class);
            return map.get("ProfileName").toString();
        }
    }

    private Platform getPlatform(OriginalDetectIntentRequest originalDetectIntentRequest){
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> map = objectMapper.convertValue(originalDetectIntentRequest,Map.class);
        if(map.get("source")!=null)return Platform.TELEGRAM;
        else return Platform.WHATSAPP;
    }
}
