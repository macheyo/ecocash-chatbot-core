package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.OriginalDetectIntentRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Platform;

import java.util.Calendar;
import java.util.Map;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DialogFlowUtil {
    public static String getAlias(OriginalDetectIntentRequest originalDetectIntentRequest) {
        if(originalDetectIntentRequest.getSource().equalsIgnoreCase(Platform.TELEGRAM.toString())){
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String,Object> map = objectMapper.convertValue(originalDetectIntentRequest.getPayload(),Map.class);
            Map<String,Object> data = objectMapper.convertValue(map.get("data"),Map.class);
            Map<String,Object> from = objectMapper.convertValue(data.get("from"),Map.class);
            return from.get("first_name").toString();
        }
        else return "valued customer";
    }

    public static String getTimeOfDay() {
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

    public static String getChatId(OriginalDetectIntentRequest originalDetectIntentRequest){
        if(originalDetectIntentRequest.getSource().equalsIgnoreCase(Platform.TELEGRAM.toString())){
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String,Object> map = objectMapper.convertValue(originalDetectIntentRequest.getPayload(),Map.class);
            Map<String,Object> data = objectMapper.convertValue(map.get("data"),Map.class);
            Map<String,Object> chat = objectMapper.convertValue(data.get("chat"),Map.class);
            return chat.get("id").toString();
        }
        return null;
    }

    public static Platform getPlatform(OriginalDetectIntentRequest originalDetectIntentRequest){
        if(originalDetectIntentRequest.getSource().equalsIgnoreCase(Platform.TELEGRAM.toString()))return Platform.TELEGRAM;
        else return Platform.WHATSAPP;
    }
}
