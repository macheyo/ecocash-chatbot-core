package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.OriginalDetectIntentRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Platform;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.CustomerRepository;

import java.util.Calendar;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DialogFlowUtil {

    public static String getAlias(OriginalDetectIntentRequest originalDetectIntentRequest,  Optional<Customer> customer) {
        if(customer.isPresent()){
            return customer.get().getFirstName();
        }
        else{
            if (getPlatform(originalDetectIntentRequest).equals(Platform.TELEGRAM)) {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> map = objectMapper.convertValue(originalDetectIntentRequest.getPayload(), Map.class);
                Map<String, Object> data = objectMapper.convertValue(map.get("data"), Map.class);
                Map<String, Object> from = objectMapper.convertValue(data.get("from"), Map.class);
                return from.get("first_name").toString();
            } else {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> map = objectMapper.convertValue(originalDetectIntentRequest.getPayload(), Map.class);
                return map.get("ProfileName").toString();
            }
        }
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

        if(getPlatform(originalDetectIntentRequest).equals(Platform.TELEGRAM)){
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String,Object> map = objectMapper.convertValue(originalDetectIntentRequest.getPayload(),Map.class);
            Map<String,Object> data = objectMapper.convertValue(map.get("data"),Map.class);
            Map<String,Object> chat = objectMapper.convertValue(data.get("chat"),Map.class);
            return chat.get("id").toString();
        }else{
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String,Object> map = objectMapper.convertValue(originalDetectIntentRequest.getPayload(),Map.class);
            return map.get("From").toString();
        }

    }

    public static Platform getPlatform(OriginalDetectIntentRequest originalDetectIntentRequest){
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> map = objectMapper.convertValue(originalDetectIntentRequest,Map.class);
        if(map.get("source")!=null)return Platform.TELEGRAM;
        else return Platform.WHATSAPP;
    }

}
