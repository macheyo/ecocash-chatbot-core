package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.OriginalDetectIntentRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Platform;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Profile;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.CustomerRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.service.CustomerService;
import zw.co.cassavasmartech.ecocashchatbotcore.service.ProfileService;

import java.util.Calendar;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class DialogFlowServiceImpl implements DialogFlowService {
    @Autowired
    ProfileService profileService;
    @Autowired
    CustomerRepository customerRepository;

    @Override
    public WebhookResponse processWebhookCall(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow transaction request {}", webhookRequest);
        switch (webhookRequest.getQueryResult().getIntent().getDisplayName()) {
            case "Default Welcome Intent":
                return welcomeIntentHandler(webhookRequest);

        }
        return null;
    }

    private WebhookResponse welcomeIntentHandler(WebhookRequest webhookRequest){
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        Optional<Customer> customer = customerRepository.findByProfilesChatId(getChatId(webhookRequest.getOriginalDetectIntentRequest()));
        String prompt="";
        if(customer.isPresent())
            prompt = String.format("Welcome back %s%s hope you are having a beautiful %s so far. What can I do for you today", customer.get().getFirstName(),new String(Character.toChars( 0x1F349)),getTimeOfDay());
        else prompt = String.format("Hi there %s and good %s to you %s! Welcome to Ecocash. My name is ***, your Ecocash Digital Assistant. I can help you to register for Ecocash and Self-Services,send money, get your statement, reset your pin, make payments, buy airtime, resolve queries and much much more via chat. To start, tell me what you need or just ask me a question, and I’ll be happy to assist you right away!",new String(Character.toChars( 0x1F349)),getTimeOfDay(),getAlias(webhookRequest.getOriginalDetectIntentRequest()));
        return WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
    }

    private String getAlias(OriginalDetectIntentRequest originalDetectIntentRequest) {
        if(originalDetectIntentRequest.getSource().equalsIgnoreCase(Platform.TELEGRAM.toString())){
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String,Object> map = objectMapper.convertValue(originalDetectIntentRequest.getPayload(),Map.class);
            Map<String,Object> data = objectMapper.convertValue(map.get("data"),Map.class);
            Map<String,Object> from = objectMapper.convertValue(data.get("from"),Map.class);
            return from.get("first_name").toString();
        }
        else return "valued customer";
    }

    private String getTimeOfDay() {
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

    private String getChatId(OriginalDetectIntentRequest originalDetectIntentRequest){
        if(originalDetectIntentRequest.getSource().equalsIgnoreCase(Platform.TELEGRAM.toString())){
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String,Object> map = objectMapper.convertValue(originalDetectIntentRequest.getPayload(),Map.class);
            Map<String,Object> data = objectMapper.convertValue(map.get("data"),Map.class);
            Map<String,Object> chat = objectMapper.convertValue(data.get("chat"),Map.class);
            return chat.get("id").toString();
        }
        return null;
    }
}