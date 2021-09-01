package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.PaymentGatewayProcessor;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.WebhookResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.ProfileNotFoundException;
import zw.co.cassavasmartech.ecocashchatbotcore.model.*;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.CustomerRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.ProfileRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.TicketRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.selfServiceCore.SelfServiceCoreProcessor;
import zw.co.cassavasmartech.ecocashchatbotcore.service.ProfileService;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DialogFlowServiceImpl implements DialogFlowService {
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    SelfServiceCoreProcessor selfServiceCoreProcessor;
    @Autowired
    PaymentGatewayProcessor paymentGatewayProcessor;

    @Override
    public WebhookResponse processWebhookCall(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow transaction request {}", webhookRequest);
        switch (webhookRequest.getQueryResult().getIntent().getDisplayName()) {
            case "Welcome":
                return welcomeIntentHandler(webhookRequest);
            case "usecase.pinreset":
                return pinresetUsecaseHandler(webhookRequest);
            case "usecase.pinreset.security.questions.affirmative":
                return pinresetUsecaseSecurityQuestionsAffirmativeHandler(webhookRequest);
            case "usecase.pinreset.security.questions.first.answer":
                return pinresetUsecaseSecurityQuestionsFirstAnswer(webhookRequest);


        }
        return null;
    }

    private WebhookResponse pinresetUsecaseSecurityQuestionsFirstAnswer(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        String[] customerAnswers = webhookRequest.getQueryResult().getQueryText().split(",");
        String prompt="";
        if(customer.isPresent()) {
            List<Answer> answers = selfServiceCoreProcessor.getAnswerByMsisdnAndAnswerStatus(customer.get().getMsisdn());
            Boolean isCorrect = true;
            int count=0;
            for(Answer answer:answers){
                if(!answer.getAnswer().equalsIgnoreCase(customerAnswers[count]))isCorrect=false;
                count++;
            }
            if(isCorrect){
                if(paymentGatewayProcessor.pinReset(customer.get().getMsisdn()).getField1().equalsIgnoreCase("200"))prompt = "Correct! "+Emoji.ThumbsUp+"I have reset your PIN. You will receive an SMS on your phone with your new temporary PIN. You will be prompted to change this when you dial *151#. Good day";
                else prompt = "Oops!! "+Emoji.Pensive+" i can not help you at this moment, the service seems not to be available. Please call this toll-free number **** for immediate assistance or check with me again in a few minutes.";
            }else prompt = "Your answers are wrong. An agent will be in touch shortly to verify your identity";
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest())+Emoji.Smiley+", but before we reset your PIN, what is your Ecocash number?";
        return WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
    }

    private WebhookResponse pinresetUsecaseSecurityQuestionsAffirmativeHandler(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        String prompt="";
        if(customer.isPresent()) {
            List<Answer> answers = selfServiceCoreProcessor.getAnswerByMsisdnAndAnswerStatus(customer.get().getMsisdn());
            int count=0;
            for(Answer answer:answers){
                count++;
                prompt+=count+". "+answer.getQuestion().getText()+"\n";
            }
            prompt += Emoji.Exclamation+"Answer the above security questions in the order given. Separate your answers by a comma e.g. soccer,patati patata,moyo";
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest())+Emoji.Smiley+", but before we reset your PIN, what is your Ecocash number?";
        return WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
    }

    private WebhookResponse pinresetUsecaseHandler(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        createTicket(webhookRequest, Usecase.PIN_RESET);
        String prompt="";
        if(customer.isPresent()) {
            if (selfServiceCoreProcessor.isEnrolled(customer.get().getMsisdn()).getIsEnrolled().equalsIgnoreCase("true"))
                prompt = "Great!! I can see you are enrolled on our self service platform" + Emoji.Smiley + "may I go on to ask your security questions to verify your identity?";
            else
                prompt = "You are currently not enrolled on our self " + Emoji.Pensive + "service platform. An agent is going to call you in a few minutes to verify your identity and reset your PIN";
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest())+Emoji.Smiley+", but before we reset your PIN, what is your Ecocash number?";
        return WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
    }

    private void createTicket(WebhookRequest webhookRequest, Usecase usecase) {
        Ticket ticket = new Ticket();
        ticket.setProfile(profileRepository.getByChatId(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest())).orElseThrow(()->new ProfileNotFoundException(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()))));
        ticket.setStatus(Status.OPEN);
        ticket.setSentimentStart(webhookRequest.getQueryResult().getSentimentAnalysisResult().getQueryTextSentiment().getScore());
        ticket.setOriginalQueryText(webhookRequest.getQueryResult().getQueryText());
        ticket.setUsecase(usecase);
        ticketRepository.save(ticket);
    }

    private WebhookResponse welcomeIntentHandler(WebhookRequest webhookRequest){
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        String prompt="";
        if(customer.isPresent())
            prompt = String.format("Welcome back %s%s%s hope you are having a beautiful %s so far. What can I do for you today?", customer.get().getFirstName(),Emoji.Smiley,Emoji.Wave,DialogFlowUtil.getTimeOfDay());
        else prompt = String.format("Hi there %s%s and good %s to you %s! Welcome to Ecocash. My name is ***, your Ecocash Digital Assistant. I can help you to register for Ecocash and Self-Services,send money, get your statement, reset your pin, make payments, buy airtime, resolve queries and much much more via chat. To start, tell me what you need or just ask me a question, and I’ll be happy to assist you right away!",Emoji.Smiley,Emoji.Wave,DialogFlowUtil.getTimeOfDay(),DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest()));
        return WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
    }

    private Optional<Customer> isNewCustomer(WebhookRequest webhookRequest){
        return customerRepository.findByProfilesChatId(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()));
    }


}