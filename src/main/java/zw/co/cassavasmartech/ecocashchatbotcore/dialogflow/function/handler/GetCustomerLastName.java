package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.OriginalDetectIntentRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.FunctionAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Platform;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PromptObject;

import java.util.Map;

public class GetCustomerLastName extends FunctionAdapter {
    @Override
    public String process(PromptObject... args) {
        if(args[0].getCustomer()!=null){
            return args[0].getCustomer().getLastName();
        }
        else{
            if (getPlatform(args[0].getWebhookRequest().getOriginalDetectIntentRequest()).equals(Platform.TELEGRAM)) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> map = objectMapper.convertValue(args[0].getWebhookRequest().getOriginalDetectIntentRequest().getPayload(), Map.class);
                Map<String, Object> data = objectMapper.convertValue(map.get("data"), Map.class);
                Map<String, Object> from = objectMapper.convertValue(data.get("from"), Map.class);
                return from.get("last_name").toString();
            } else {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> map = objectMapper.convertValue(args[0].getWebhookRequest().getOriginalDetectIntentRequest().getPayload(), Map.class);
                return "";
            }
        }
    }

    private Platform getPlatform(OriginalDetectIntentRequest originalDetectIntentRequest){
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> map = objectMapper.convertValue(originalDetectIntentRequest,Map.class);
        if(map.get("source")!=null)return Platform.TELEGRAM;
        else return Platform.WHATSAPP;
    }
}
