package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Emoji;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.CustomerRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.service.ProfileService;

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
            case "Welcome":
                return welcomeIntentHandler(webhookRequest);
            case "usecase.pinreset":
                return pinresetUsecaseHandler(webhookRequest);

        }
        return null;
    }

    private WebhookResponse pinresetUsecaseHandler(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        String prompt="";
        if(customer.isPresent())

            prompt = String.format("Welcome back %s%s hope you are having a beautiful %s so far. What can I do for you today?", customer.get().getFirstName(), Emoji.Smiley,DialogFlowUtil.getTimeOfDay());
        else prompt = String.format("Hi there %s and good %s to you %s! Welcome to Ecocash. My name is ***, your Ecocash Digital Assistant. I can help you to register for Ecocash and Self-Services,send money, get your statement, reset your pin, make payments, buy airtime, resolve queries and much much more via chat. To start, tell me what you need or just ask me a question, and I’ll be happy to assist you right away!",new String(Character.toChars( 0x1F349)),DialogFlowUtil.getTimeOfDay(),DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest()));
        return WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
    }

    private WebhookResponse welcomeIntentHandler(WebhookRequest webhookRequest){
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        String prompt="";
        if(customer.isPresent())
            prompt = String.format("Welcome back %s%s hope you are having a beautiful %s so far. What can I do for you today?", customer.get().getFirstName(),Emoji.Smiley,DialogFlowUtil.getTimeOfDay());
        else prompt = String.format("Hi there %s and good %s to you %s! Welcome to Ecocash. My name is ***, your Ecocash Digital Assistant. I can help you to register for Ecocash and Self-Services,send money, get your statement, reset your pin, make payments, buy airtime, resolve queries and much much more via chat. To start, tell me what you need or just ask me a question, and I’ll be happy to assist you right away!",Emoji.Smiley,DialogFlowUtil.getTimeOfDay(),DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest()));
        return WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
    }

    private Optional<Customer> isNewCustomer(WebhookRequest webhookRequest){
        return customerRepository.findByProfilesChatId(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()));
    }


}