package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.common.MobileNumberFormater;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.PaymentGatewayProcessor;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.data.BillerLookupRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.data.SubscriberAirtimeRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.data.SubscriberToBillerRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.data.SubscriberToSubscriberRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.*;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.EipTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.Merchant;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.MerchantRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.ProfileNotFoundException;
import zw.co.cassavasmartech.ecocashchatbotcore.model.*;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.CustomerRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.ProfileRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.TicketRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.selfServiceCore.SelfServiceCoreProcessor;
import zw.co.cassavasmartech.ecocashchatbotcore.service.CustomerService;
import zw.co.cassavasmartech.ecocashchatbotcore.service.ProfileService;
import zw.co.cassavasmartech.ecocashchatbotcore.statementProcessor.StatementServiceConfigurationProperties;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//TODO pin reset close off intents
@Service
@Slf4j
public class DialogFlowServiceImpl implements DialogFlowService {
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    ProfileService profileService;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    SelfServiceCoreProcessor selfServiceCoreProcessor;
    @Autowired
    PaymentGatewayProcessor paymentGatewayProcessor;
    @Autowired
    CustomerService customerService;
    @Autowired
    StatementServiceConfigurationProperties statementServiceConfigurationProperties;
    @Autowired
    MerchantRepository merchantRepository;
    @Autowired
    MobileNumberFormater mobileNumberFormater;

    @Override
    public WebhookResponse processWebhookCall(WebhookRequest webhookRequest) throws ParseException {
        log.info("Processing dialogflow transaction request {}\n", webhookRequest);
        switch (webhookRequest.getQueryResult().getIntent().getDisplayName()) {
            case "Default Welcome Intent":
                return welcomeHandler(webhookRequest);
            case "usecase.pinreset":
                return pinresetUsecaseHandler(webhookRequest);
            case "usecase.pinreset.security.questions.affirmative":
                return pinresetUsecaseSecurityQuestionsAffirmativeHandler(webhookRequest);
            case "usecase.pinreset.security.questions.first.answer":
                return pinresetUsecaseSecurityQuestionsFirstAnswerHandler(webhookRequest);
            case "usecase.pay.biller.scenario1":
                return payBillerUsecaseScenario1Handler(webhookRequest);
            case "usecase.pay.biller.scenario2":
                return payBillerUsecaseScenario2Handler(webhookRequest);
            case "usecase.pay.biller.get.biller.amount":
                return payBillerUsecaseGetBillerAmountHandler(webhookRequest);
            case "usecase.pay.biller.get.biller.code":
                return payBillerUsecaseGetBillerCodeHandler(webhookRequest);
            case "usecase.pay.biller.get.account.intent":
                return payBillerUsecaseGetBillerAccountHandler(webhookRequest);
            case "usecase.pay.biller.get.biller.confirmation.affirmative":
                return payBillerUsecaseGetBillerConfirmationAffirmativeHandler(webhookRequest);
            case "usecase.pay.biller.get.biller.confirmation.negative":
                return payBillerUsecaseGetBillerConfirmationNegativeHandler(webhookRequest);
            case "usecase.statement.scenario.1":
                return statementUsecaseScenario1Handler(webhookRequest);
            case "usecase.statement.scenario.2":
                return statementUsecaseScenario2Handler(webhookRequest);
            case "usecase.statement.scenario.3":
                return statementUsecaseScenario3Handler(webhookRequest);
            case "usecase.statement.end.date":
                return statementUsecaseEndDateHandler(webhookRequest);
            case "usecase.statement.start.date":
                return statementUsecaseStartDateHandler(webhookRequest);
            case "usecase.pay.merchant.scenario1":
                return payMerchantUsecaseScenario1Handler(webhookRequest);
            case "usecase.pay.merchant.scenario2":
                return payMerchantUsecaseScenario2Handler(webhookRequest);
            case "usecase.pay.merchant.get.msisdn":
                return payMerchantUsecaseGetMsisdnOrNameHandler(webhookRequest);
            case "usecase.pay.merchant.get.name":
                return payMerchantUsecaseGetMsisdnOrNameHandler(webhookRequest);
            case "usecase.pay.merchant.get.amount":
                return payMerchantUsecaseGetAmountHandler(webhookRequest);
            case "usecase.pay.merchant.confirmation.affirmative":
                return payMerchantUsecaseConfirmationAffirmativeHandler(webhookRequest);
            case "usecase.send.airtime.scenario1":
                return sendAirtimeUsecaseScenario1Handler(webhookRequest);
            case "usecase.send.airtime.scenario2":
                return sendAirtimeUsecaseScenario2Handler(webhookRequest);
            case "usecase.send.airtime.myself":
                return sendAirtimeUsecaseMyselfHandler(webhookRequest);
            case "usecase.send.airtime.myself.negative":
                return sendAirtimeUsecaseMyselfNegativeHandler(webhookRequest);
            case "usecase.send.airtime.get.beneficiary.msisdn":
                return sendAirtimeUsecaseGetBeneficiaryHandler(webhookRequest);
            case "usecase.send.airtime.get.beneficiary.amount":
                return sendAirtimeUsecaseGetAmountHandler(webhookRequest);
            case "usecase.send.airtime.confirmation.affirmative":
                return sendAirtimeUsecaseConfirmationAffirmativeHandler(webhookRequest);
            case "usecase.send.money.scenario1":
                return sendMoneyUsecaseScenario1Handler(webhookRequest);
            case "usecase.send.money.scenario2":
                return sendMoneyUsecaseScenario2Handler(webhookRequest);
            case "usecase.send.money.get.beneficiary.msisdn":
                return sendMoneyUsecaseGetBeneficiaryMsisdnHandler(webhookRequest);
            case "usecase.send.money.get.beneficiary.amount":
                return sendMoneyUsecaseGetBeneficiaryAmountHandler(webhookRequest);
            case "usecase.send.money.confirmation.affirmative":
                return sendMoneyUsecaseConfimationAffirmativeHandler(webhookRequest);
            case "usecase.verify.get.msisdn":
                return verifyUsecaseGetMsisdnHandler(webhookRequest);
            case "usecase.verify.get.otp":
                return verifyUsecaseGetOtpHandler(webhookRequest);


        }
        return null;
    }

    private WebhookResponse sendMoneyUsecaseConfimationAffirmativeHandler(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        ObjectMapper objectMapper = new ObjectMapper();
        int ticketIndex=0;
        for(int i = 0;i<outputContexts.size();i++){
            Map<String,Object> map = objectMapper.convertValue(outputContexts.get(i),Map.class);
            if(map.get("name").toString().equalsIgnoreCase(webhookRequest.getSession()+"/contexts/ticket")) ticketIndex=i;
        }
        Map<String,Object> map = objectMapper.convertValue(outputContexts.get(ticketIndex).getParameters(),Map.class);
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        String prompt=null;
        if(customer.isPresent()) {
            TransactionResponse transactionResponse = paymentGatewayProcessor.subscriberToSubscriber(SubscriberToSubscriberRequest.builder()
                    .msisdn1(customer.get().getMsisdn())
                    .msisdn2(map.get("msisdn").toString())
                    .amount(map.get("amount").toString())
                    .ticketId(Double.valueOf(map.get("id").toString()).longValue()).build());
            if (transactionResponse.getField1().equalsIgnoreCase("603"))
                prompt = "Great!!" + Emoji.Smiley + "Processing your payment right now.\nYou will receive a prompt on your phone for you to enter your PIN.\n" + Emoji.Exclamation + "You have to enter the correct PIN to complete this transaction";
            else
                prompt = "Oops!!" + Emoji.Pensive + "Something went wrong with the transaction\nWould you like to try this again?";

        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest())+Emoji.Smiley+", but before we buy your airtime, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse sendMoneyUsecaseGetBeneficiaryAmountHandler(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        ObjectMapper objectMapper = new ObjectMapper();
        int ticketIndex=0;
        for(int i = 0;i<outputContexts.size();i++){
            Map<String,Object> map = objectMapper.convertValue(outputContexts.get(i),Map.class);
            if(map.get("name").toString().equalsIgnoreCase(webhookRequest.getSession()+"/contexts/ticket")) ticketIndex=i;
        }
        Map<String,Object> map = objectMapper.convertValue(outputContexts.get(ticketIndex).getParameters(),Map.class);
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        TransactionResponse transactionResponse = customerService.customerLookup(SubscriberDto.builder().msisdn(map.get("msisdn").toString()).build());
        String prompt = null;
        if(customer.isPresent()) {
            prompt = "Alright thats fine "+DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest())+","+Emoji.Smiley+"\nSo in summary you want to send $ZWL"+ map.get("amount").toString()+" to "+map.get("msisdn").toString()+" ("+transactionResponse.getField6()+" "+transactionResponse.getField9()+")\ncan you confirm this is correct?";
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest())+Emoji.Smiley+", but before we pay sen your money, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse sendMoneyUsecaseGetBeneficiaryMsisdnHandler(WebhookRequest webhookRequest) {
        String prompt = "Got it"+Emoji.Smiley+"How much do you want to send?";
        return getWebhookResponse(webhookRequest,prompt,null,Usecase.SEND_MONEY);
    }

    private WebhookResponse sendMoneyUsecaseScenario2Handler(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> map = objectMapper.convertValue(outputContexts.get(0).getParameters(),Map.class);
        Map<String,Object> payment = objectMapper.convertValue(map.get("payment"),Map.class);
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        TransactionResponse transactionResponse = customerService.customerLookup(SubscriberDto.builder().msisdn(map.get("msisdn").toString()).build());
        String prompt = null;
        if(customer.isPresent()) {
            prompt = "Alright thats fine "+DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest())+","+Emoji.Smiley+"\nSo in summary you want to send $ZWL"+ payment.get("amount").toString()+" to "+map.get("msisdn").toString()+" ("+transactionResponse.getField6()+" "+transactionResponse.getField9()+")\ncan you confirm this is correct?";
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest())+Emoji.Smiley+", but before we buy your airtime, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .outputContexts(createTicket(webhookRequest,Usecase.SEND_MONEY))
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse sendMoneyUsecaseScenario1Handler(WebhookRequest webhookRequest) {
        String prompt = "Alright, lets quickly do that"+Emoji.Smiley+" what is the Ecocash number you want to send money to?";
        return getWebhookResponse(webhookRequest,prompt,createTicket(webhookRequest,Usecase.SEND_MONEY),Usecase.SEND_MONEY);
    }

    private WebhookResponse verifyUsecaseGetOtpHandler(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        ObjectMapper objectMapper = new ObjectMapper();
        int ticketIndex=0;
        for(int i = 0;i<outputContexts.size();i++){
            Map<String,Object> map = objectMapper.convertValue(outputContexts.get(i),Map.class);
            if(map.get("name").toString().equalsIgnoreCase(webhookRequest.getSession()+"/contexts/ticket")) ticketIndex=i;
        }
        Map<String,Object> map = objectMapper.convertValue(outputContexts.get(ticketIndex).getParameters(),Map.class);
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        String prompt=null;
        if(customer.isPresent()) {
            if(profileService.verifyCustomer(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()),webhookRequest.getQueryResult().getQueryText()))
                prompt = "Great! you have been verified"+Emoji.ThumbsUp+Emoji.Grin+"Now that we have that out of the way...";
            else prompt = "Unfortunately I have failed to verify you. The verification code you entered is either expired or invalid...";
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest())+Emoji.Smiley+", but before we buy your airtime, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;

    }

    private WebhookResponse verifyUsecaseGetMsisdnHandler(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        Customer customer = new Customer();
        customer.setMsisdn(mobileNumberFormater.formatMsisdnMinimum(webhookRequest.getQueryResult().getQueryText()));
        Customer newCustomer = customerService.save(customer);
        String prompt="Oops!"+Emoji.Pensive+" Something went wrong, please try again later";
        if(customerService.customerLookup(SubscriberDto.builder().msisdn(mobileNumberFormater.formatMsisdnMinimum(webhookRequest.getQueryResult().getQueryText())).build()).getField1().equalsIgnoreCase("200")){
            if (newCustomer != null) {
                Profile profile = new Profile();
                profile.setAlias(DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest()));
                profile.setChatId(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()));
                profile.setPlatform(DialogFlowUtil.getPlatform(webhookRequest.getOriginalDetectIntentRequest()));
                Profile newProfile = profileService.save(newCustomer.getMsisdn(), profile);
                if (newProfile != null) {
                    prompt = "Great! got it" + Emoji.ThumbsUp + "\nI have sent a verification code via sms to your Ecocash number, enter that code here to verify your identity. \nThe code is only valid for 2 minutes" + Emoji.Exclamation;
                    profileService.generateOtp(newProfile.getChatId());
                }
            }
        }else prompt = "Unfortunately, this number "+Emoji.PointUp+"is not registered on Ecocash "+Emoji.Disappointed+" \nCheck again, is the number "+webhookRequest.getQueryResult().getQueryText()+" correct?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse sendAirtimeUsecaseConfirmationAffirmativeHandler(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        ObjectMapper objectMapper = new ObjectMapper();
        int ticketIndex=0;
        for(int i = 0;i<outputContexts.size();i++){
            Map<String,Object> map = objectMapper.convertValue(outputContexts.get(i),Map.class);
            if(map.get("name").toString().equalsIgnoreCase(webhookRequest.getSession()+"/contexts/ticket")) ticketIndex=i;
        }
        Map<String,Object> map = objectMapper.convertValue(outputContexts.get(ticketIndex).getParameters(),Map.class);
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        String prompt=null;
        if(customer.isPresent()) {
                TransactionResponse transactionResponse = paymentGatewayProcessor.subscriberAirtime(SubscriberAirtimeRequest.builder()
                                .msisdn1(customer.get().getMsisdn())
                                .msisdn2(map.get("msisdn").toString())
                                .amount(map.get("amount").toString())
                                .ticketId(Double.valueOf(map.get("id").toString()).longValue()).build());
                if (transactionResponse.getField1().equalsIgnoreCase("603"))
                    prompt = "Great!!" + Emoji.Smiley + "Processing your payment right now.\nYou will receive a prompt on your phone for you to enter your PIN.\n" + Emoji.Exclamation + "You have to enter the correct PIN to complete this transaction";
                else
                    prompt = "Oops!!" + Emoji.Pensive + "Something went wrong with the transaction\nWould you like to try this again?";

        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest())+Emoji.Smiley+", but before we buy your airtime, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse sendAirtimeUsecaseGetAmountHandler(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        ObjectMapper objectMapper = new ObjectMapper();
        int ticketIndex=0;
        for(int i = 0;i<outputContexts.size();i++){
            Map<String,Object> map = objectMapper.convertValue(outputContexts.get(i),Map.class);
            if(map.get("name").toString().equalsIgnoreCase(webhookRequest.getSession()+"/contexts/ticket")) ticketIndex=i;
        }
        Map<String,Object> map = objectMapper.convertValue(outputContexts.get(ticketIndex).getParameters(),Map.class);
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        TransactionResponse transactionResponse = customerService.customerLookup(SubscriberDto.builder().msisdn(map.get("msisdn").toString()).build());
        String prompt = null;
        if(customer.isPresent()) {
            prompt = "Alright thats fine "+DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest())+","+Emoji.Smiley+"\nSo in summary you want to buy $ZWL"+ map.get("amount").toString()+" for "+map.get("msisdn").toString()+" ("+transactionResponse.getField6()+" "+transactionResponse.getField9()+")\ncan you confirm this is correct?";
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest())+Emoji.Smiley+", but before we pay your bill, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse sendAirtimeUsecaseGetBeneficiaryHandler(WebhookRequest webhookRequest) {
        String prompt = "Got it"+Emoji.Smiley+"How much airtime do you want to buy?";
        return getWebhookResponse(webhookRequest,prompt,null,Usecase.BUY_AIRTIME);
    }

    private WebhookResponse sendAirtimeUsecaseMyselfNegativeHandler(WebhookRequest webhookRequest) {
        String prompt = "what is the Econet number you want to buy airtime for?";
        return getWebhookResponse(webhookRequest,prompt,null,Usecase.BUY_AIRTIME);
    }

    private WebhookResponse sendAirtimeUsecaseMyselfHandler(WebhookRequest webhookRequest) {
        String prompt = "Got it"+Emoji.Smiley+"How much airtime do you want to buy?";
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        ObjectMapper objectMapper = new ObjectMapper();
        int ticketIndex=0;
        for(int i = 0;i<outputContexts.size();i++){
            Map<String,Object> map = objectMapper.convertValue(outputContexts.get(i),Map.class);
            if(map.get("name").toString().equalsIgnoreCase(webhookRequest.getSession()+"/contexts/ticket")) ticketIndex=i;
        }
        Map<String,Object> map = objectMapper.convertValue(outputContexts.get(ticketIndex).getParameters(),Map.class);
        Optional<Customer> customer = customerRepository.findByProfilesChatId(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()));
        return getWebhookResponse(webhookRequest,prompt,new Object[]{OutputContext.builder()
                .lifespanCount(50)
                .name(webhookRequest.getSession()+"/contexts/ticket")
                .parameters(TicketParameter.builder().id(Double.valueOf(map.get("id").toString()).longValue()).msisdn2(customer.get().getMsisdn()).build())
                .build()},Usecase.BUY_AIRTIME);
    }

    private WebhookResponse sendAirtimeUsecaseScenario2Handler(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> map = objectMapper.convertValue(outputContexts.get(0).getParameters(),Map.class);
        Map<String,Object> payment = objectMapper.convertValue(map.get("payment"),Map.class);
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        TransactionResponse transactionResponse = customerService.customerLookup(SubscriberDto.builder().msisdn(map.get("msisdn").toString()).build());
        String prompt = null;
        if(customer.isPresent()) {
            prompt = "Alright thats fine "+DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest())+","+Emoji.Smiley+"\nSo in summary you want to buy $ZWL"+ payment.get("amount").toString()+" for "+map.get("msisdn").toString()+" ("+transactionResponse.getField6()+" "+transactionResponse.getField9()+")\ncan you confirm this is correct?";
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest())+Emoji.Smiley+", but before we buy your airtime, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .outputContexts(createTicket(webhookRequest,Usecase.BUY_AIRTIME))
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse sendAirtimeUsecaseScenario1Handler(WebhookRequest webhookRequest) {

        String prompt = "Alright, lets quickly do that"+Emoji.Smiley+" are you buying airtime for yourself?";
        return getWebhookResponse(webhookRequest,prompt,createTicket(webhookRequest,Usecase.BUY_AIRTIME),Usecase.BUY_AIRTIME);
    }

    private WebhookResponse payMerchantUsecaseConfirmationAffirmativeHandler(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        ObjectMapper objectMapper = new ObjectMapper();
        int ticketIndex=0;
        for(int i = 0;i<outputContexts.size();i++){
            Map<String,Object> map = objectMapper.convertValue(outputContexts.get(i),Map.class);
            if(map.get("name").toString().equalsIgnoreCase(webhookRequest.getSession()+"/contexts/ticket")) ticketIndex=i;
        }
        Map<String,Object> map = objectMapper.convertValue(outputContexts.get(ticketIndex).getParameters(),Map.class);
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        Optional<Merchant> merchant = merchantRepository.findByNameOrMerchantCode(map.get("msisdn.original").toString(),map.get("msisdn.original").toString());
        String prompt=null;
        if(customer.isPresent()) {
            if(merchant.isPresent()) {
                EipTransaction eipTransaction = customerService.payMerchant(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()),zw.co.cassavasmartech.ecocashchatbotcore.eip.data.SubscriberToMerchantRequest.builder()
                        .msisdn(customer.get().getMsisdn())
                        .merchantCode(merchant.get().getMerchantCode())
                        .amount(BigDecimal.valueOf(Double.parseDouble(map.get("amount").toString())))
                        .ticketId(Double.valueOf(map.get("id").toString()).longValue())
                        .build());
                if (eipTransaction!=null)
                    prompt = "Great!!" + Emoji.Smiley + "Processing your payment right now.\nYou will receive a prompt on your phone for you to enter your PIN.\n" + Emoji.Exclamation + "You have to enter the correct PIN to complete this transaction";
                else
                    prompt = "Oops!!" + Emoji.Pensive + "Something went wrong with the transaction\nWould you like to try this again?";
            }
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest())+Emoji.Smiley+", but before we pay your bill, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse payMerchantUsecaseGetAmountHandler(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        ObjectMapper objectMapper = new ObjectMapper();
        int ticketIndex=0;
        for(int i = 0;i<outputContexts.size();i++){
            Map<String,Object> map = objectMapper.convertValue(outputContexts.get(i),Map.class);
            if(map.get("name").toString().equalsIgnoreCase(webhookRequest.getSession()+"/contexts/ticket")) ticketIndex=i;
        }
        Map<String,Object> map = objectMapper.convertValue(outputContexts.get(ticketIndex).getParameters(),Map.class);
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        Optional<Merchant> merchant = merchantRepository.findByNameOrMerchantCode(map.get("msisdn").toString(),map.get("msisdn").toString());
        String prompt = null;
        if(customer.isPresent()) {
            if(merchant.isPresent())
            prompt = "Alright thats fine,"+Emoji.Smiley+"\nSo in summary you want to pay $ZWL"+ map.get("amount").toString() +" to merchant code "+merchant.get().getMerchantCode()+" ("+merchant.get().getName()+")\ncan you confirm this is correct?";
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest())+Emoji.Smiley+", but before we pay your bill, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse payMerchantUsecaseGetMsisdnOrNameHandler(WebhookRequest webhookRequest) {
        String prompt = "Got it"+Emoji.Smiley+"How much do you want to pay?";
        return getWebhookResponse(webhookRequest,prompt,null,Usecase.MERCHANT_PAYMENT);
    }

    private WebhookResponse payMerchantUsecaseScenario2Handler(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> map = objectMapper.convertValue(outputContexts.get(0).getParameters(),Map.class);
        Map<String,Object> payment = objectMapper.convertValue(map.get("payment"),Map.class);
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        Optional<Merchant> merchant = merchantRepository.findByNameOrMerchantCode(map.get("msisdn").toString(),map.get("msisdn").toString());
        String prompt = null;
        if(customer.isPresent()) {
            if(merchant.isPresent())
                prompt = "Alright thats fine,"+Emoji.Smiley+"\nSo in summary you want to pay $ZWL"+ payment.get("amount").toString() +" to merchant code "+merchant.get().getMerchantCode()+" ("+merchant.get().getName()+")\ncan you confirm this is correct?";
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest())+Emoji.Smiley+", but before we pay your bill, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .outputContexts(createTicket(webhookRequest,Usecase.MERCHANT_PAYMENT))
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse payMerchantUsecaseScenario1Handler(WebhookRequest webhookRequest) {
        String prompt = "Alright, lets quickly do that"+Emoji.Smiley+" what is the Merchant code or Name of the Merchant you want to pay?";
        return getWebhookResponse(webhookRequest,prompt,createTicket(webhookRequest,Usecase.MERCHANT_PAYMENT),Usecase.MERCHANT_PAYMENT);
    }

    private WebhookResponse statementUsecaseStartDateHandler(WebhookRequest webhookRequest) {
        String prompt = "Ok to which date would you want the statement to be generated "+Emoji.Date+"?";
        return getWebhookResponse(webhookRequest,prompt,null,Usecase.SUBSCRIBER_STATEMENT);
    }

    private WebhookResponse statementUsecaseEndDateHandler(WebhookRequest webhookRequest) throws ParseException {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        ObjectMapper objectMapper = new ObjectMapper();
        int ticketIndex=0;
        for(int i = 0;i<outputContexts.size();i++){
            Map<String,Object> map = objectMapper.convertValue(outputContexts.get(i),Map.class);
            if(map.get("name").toString().equalsIgnoreCase(webhookRequest.getSession()+"/contexts/ticket")) ticketIndex=i;
        }
        Map<String,Object> map = objectMapper.convertValue(outputContexts.get(ticketIndex).getParameters(),Map.class);Optional<Customer> customer = isNewCustomer(webhookRequest);
        String prompt;
        if(customer.isPresent()) {
            Statement statement = customerService.getStatement(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()), StatementRequest.builder()
                            .endDate(map.get("startDateTime").toString())
                            .startDate(map.get("endDateTime").toString())
                            .msisdn(customer.get().getMsisdn())
                            .mime("PDF")
                            .build());
            if(statement!=null)
                prompt = "Done"+Emoji.Smiley+Emoji.ThumbsUp+"Here you go, please click the link below to download"+Emoji.PointDown+"\n"+statement.getFileDownloadUri().replace("http://217.15.118.15",statementServiceConfigurationProperties.getNgrokServiceEndpointUrl())+"\n\nIs there anything else I can assist you with?"+Emoji.Smiley;
            else prompt = "Oops!!"+Emoji.Pensive+"Something went wrong with the transaction\nWould you like to try this again?";
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest())+Emoji.Smiley+", but before we get your statement, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse statementUsecaseScenario3Handler(WebhookRequest webhookRequest) throws ParseException {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        ObjectMapper objectMapper = new ObjectMapper();
        int ticketIndex=0;
        for(int i = 0;i<outputContexts.size();i++){
            Map<String,Object> map = objectMapper.convertValue(outputContexts.get(i),Map.class);
            if(map.get("name").toString().equalsIgnoreCase(webhookRequest.getSession()+"/contexts/ticket")) ticketIndex=i;
        }
        Map<String,Object> map = objectMapper.convertValue(outputContexts.get(ticketIndex).getParameters(),Map.class);
        Map<String,Object> datePeriod = objectMapper.convertValue(map.get("datePeriod"),Map.class);
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        Object[] context=null;
        String prompt;
        if(customer.isPresent()) {
            context = createTicket(webhookRequest, Usecase.SUBSCRIBER_STATEMENT);
            Statement statement = customerService.getStatement(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()), StatementRequest.builder()
                    .endDate(datePeriod.get("startDate").toString())
                    .startDate(datePeriod.get("endDate").toString())
                    .msisdn(customer.get().getMsisdn())
                    .mime("PDF")
                    .build());
            if(statement!=null)
                prompt = "Done"+Emoji.Smiley+Emoji.ThumbsUp+"Here you go, please click the link below to download"+Emoji.PointDown+"\n"+statement.getFileDownloadUri().replace("http://217.15.118.15",statementServiceConfigurationProperties.getNgrokServiceEndpointUrl())+"\n\nIs there anything else I can assist you with?"+Emoji.Smiley;
            else prompt = "Oops!!"+Emoji.Pensive+"Something went wrong with the transaction\nWould you like to try this again?";
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest())+Emoji.Smiley+", but before we get your statement, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .outputContexts(context)
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse statementUsecaseScenario2Handler(WebhookRequest webhookRequest) throws ParseException {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        ObjectMapper objectMapper = new ObjectMapper();
        int ticketIndex=0;
        for(int i = 0;i<outputContexts.size();i++){
            Map<String,Object> map = objectMapper.convertValue(outputContexts.get(i),Map.class);
            if(map.get("name").toString().equalsIgnoreCase(webhookRequest.getSession()+"/contexts/ticket")) ticketIndex=i;
        }
        Map<String,Object> map = objectMapper.convertValue(outputContexts.get(ticketIndex).getParameters(),Map.class);
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        Object[] context=null;
        String prompt;
        if(customer.isPresent()) {
            context = createTicket(webhookRequest, Usecase.SUBSCRIBER_STATEMENT);
            Statement statement = customerService.getStatement(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()), StatementRequest.builder()
                    .endDate(map.get("startdate").toString())
                    .startDate(map.get("enddate").toString())
                    .msisdn(customer.get().getMsisdn())
                    .mime("PDF")
                    .build());
            if(statement!=null)
                prompt = "Done"+Emoji.Smiley+Emoji.ThumbsUp+"Here you go, please click the link below to download"+Emoji.PointDown+"\n"+statement.getFileDownloadUri().replace("http://217.15.118.15",statementServiceConfigurationProperties.getNgrokServiceEndpointUrl())+"\n\nIs there anything else I can assist you with?"+Emoji.Smiley;
            else prompt = "Oops!!"+Emoji.Pensive+"Something went wrong with the transaction\nWould you like to try this again?";
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest())+Emoji.Smiley+", but before we get your statement, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .outputContexts(context)
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse statementUsecaseScenario1Handler(WebhookRequest webhookRequest) {
        String prompt = "Alright, lets quickly do that"+Emoji.Smiley+" from which date "+Emoji.Date+" would you want the statement to be generated?";
        return getWebhookResponse(webhookRequest,prompt,createTicket(webhookRequest,Usecase.SUBSCRIBER_STATEMENT),Usecase.SUBSCRIBER_STATEMENT);
    }

    private WebhookResponse payBillerUsecaseGetBillerConfirmationNegativeHandler(WebhookRequest webhookRequest) {
        String prompt = "That's great!!"+Emoji.Smiley+" Thank you for using EcoCash "+DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest())+". Continue living life the EcoCash way!!!";
        return getWebhookResponse(webhookRequest, prompt, null,Usecase.BILL_PAYMENT);
    }

    private WebhookResponse payBillerUsecaseGetBillerConfirmationAffirmativeHandler(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        ObjectMapper objectMapper = new ObjectMapper();
        int ticketIndex=0;
        for(int i = 0;i<outputContexts.size();i++){
            Map<String,Object> map = objectMapper.convertValue(outputContexts.get(i),Map.class);
            if(map.get("name").toString().equalsIgnoreCase(webhookRequest.getSession()+"/contexts/ticket")) ticketIndex=i;
        }
        Map<String,Object> map = objectMapper.convertValue(outputContexts.get(ticketIndex).getParameters(),Map.class);
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        String prompt;
        if(customer.isPresent()) {
            TransactionResponse transactionResponse = paymentGatewayProcessor.subscriberToBiller(SubscriberToBillerRequest.builder()
                    .msisdn(customer.get().getMsisdn())
                    .billerCode(map.get("biller.original").toString())
                    .amount(BigDecimal.valueOf(Double.parseDouble(map.get("amount").toString())) )
                    .msisdn2(map.get("number.original").toString())
                    .ticketId(Double.valueOf(map.get("id").toString()).longValue())
                    .build());
            if(transactionResponse.getField1().equalsIgnoreCase("603"))
            prompt = "Great!!"+Emoji.Smiley+"Processing your payment right now.\nYou will receive a prompt on your phone for you to enter your PIN.\n"+Emoji.Exclamation+"You have to enter the correct PIN to complete this transaction";
            else prompt = "Oops!!"+Emoji.Pensive+"Something went wrong with the transaction:\n"+transactionResponse.getField2()+"\nWould you like to try this again?";
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest())+Emoji.Smiley+", but before we pay your bill, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse payBillerUsecaseGetBillerAccountHandler(WebhookRequest webhookRequest) {
        String prompt = "Alrighty"+Emoji.Smiley+" How much do you want to pay?";
        return getWebhookResponse(webhookRequest,prompt,null,Usecase.BILL_PAYMENT);
    }

    private WebhookResponse payBillerUsecaseGetBillerCodeHandler(WebhookRequest webhookRequest) {
        String  prompt = "Got it"+Emoji.Smiley+" What is your Account Number?";
        return getWebhookResponse(webhookRequest,prompt,null,Usecase.BILL_PAYMENT);
    }

    private WebhookResponse payBillerUsecaseGetBillerAmountHandler(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        BillerLookupRequest billerLookupRequest = new BillerLookupRequest();
        ObjectMapper objectMapper = new ObjectMapper();
        int ticketIndex=0;
        for(int i = 0;i<outputContexts.size();i++){
            Map<String,Object> map = objectMapper.convertValue(outputContexts.get(i),Map.class);
            if(map.get("name").toString().equalsIgnoreCase(webhookRequest.getSession()+"/contexts/ticket")) ticketIndex=i;
        }
        Map<String,Object> map = objectMapper.convertValue(outputContexts.get(ticketIndex).getParameters(),Map.class);
        billerLookupRequest.setBiller(map.get("biller.original").toString());
        TransactionResponse transactionResponse = paymentGatewayProcessor.lookupBiller(billerLookupRequest);
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        String prompt;
        if(customer.isPresent()) {
            prompt = "Alright thats fine,"+Emoji.Smiley+"\nSo in summary you want to pay $ZWL"+ map.get("amount").toString() +" to biller code "+map.get("biller.original").toString()+" ("+transactionResponse.getField6()+") for account "+map.get("number.original").toString()+"\ncan you confirm this is correct?";
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest())+Emoji.Smiley+", but before we pay your bill, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;

    }

    private WebhookResponse payBillerUsecaseScenario2Handler(WebhookRequest webhookRequest) {
        return null;
    }

    private WebhookResponse payBillerUsecaseScenario1Handler(WebhookRequest webhookRequest) {
        String prompt = "I can help you do that"+Emoji.Smiley+"\nWhat is the biller code or Name of the organisation you want to pay?";
        return getWebhookResponse(webhookRequest,prompt,createTicket(webhookRequest,Usecase.BILL_PAYMENT),Usecase.BILL_PAYMENT);
    }

    private WebhookResponse pinresetUsecaseSecurityQuestionsFirstAnswerHandler(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        String[] customerAnswers = webhookRequest.getQueryResult().getQueryText().split(",");
        String prompt;
        if(customer.isPresent()) {
            List<Answer> answers = selfServiceCoreProcessor.getAnswerByMsisdnAndAnswerStatus(customer.get().getMsisdn());
            boolean isCorrect = true;
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
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse pinresetUsecaseSecurityQuestionsAffirmativeHandler(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        StringBuilder prompt= new StringBuilder();
        if(customer.isPresent()) {
            List<Answer> answers = selfServiceCoreProcessor.getAnswerByMsisdnAndAnswerStatus(customer.get().getMsisdn());
            int count=0;
            for(Answer answer:answers){
                count++;
                prompt.append(count).append(". ").append(answer.getQuestion().getText()).append("\n");
            }
            prompt.append(Emoji.Exclamation + "Answer the above security questions in the order given. Separate your answers by a comma e.g. soccer,patati patata,moyo");
        }
        else prompt = new StringBuilder("Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest()) + Emoji.Smiley + ", but before we reset your PIN, what is your Ecocash number?");
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt.toString())
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse pinresetUsecaseHandler(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        String prompt;
        if(customer.isPresent()) {
            if (selfServiceCoreProcessor.isEnrolled(customer.get().getMsisdn()).getIsEnrolled().equalsIgnoreCase("true"))
                prompt = "Great!! I can see you are enrolled on our self service platform" + Emoji.Smiley + "may I go on to ask your security questions to verify your identity?";
            else
                prompt = "You are currently not enrolled on our self " + Emoji.Pensive + "service platform. An agent is going to call you in a few minutes to verify your identity and reset your PIN";
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest())+Emoji.Smiley+", but before we reset your PIN, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .outputContexts(createTicket(webhookRequest, Usecase.PIN_RESET))
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse welcomeHandler(WebhookRequest webhookRequest){
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        String prompt;
        if(customer.isPresent())
            prompt = String.format("Welcome back %s%s%s hope you are having a beautiful %s so far. What can I do for you today?", customer.get().getFirstName(),Emoji.Smiley,Emoji.Wave,DialogFlowUtil.getTimeOfDay());
        else prompt = String.format("Hi there %s%s and good %s to you %s! Welcome to Ecocash. My name is ***, your Ecocash Digital Assistant. I can help you to register for Ecocash and Self-Services,send money, get your statement, reset your pin, make payments, buy airtime, resolve queries and much much more via chat. To start, tell me what you need or just ask me a question, and I’ll be happy to assist you right away!",Emoji.Smiley,Emoji.Wave,DialogFlowUtil.getTimeOfDay(),DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest()));
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private Optional<Customer> isNewCustomer(WebhookRequest webhookRequest){
        return customerRepository.findByProfilesChatId(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()));
    }

    private WebhookResponse getWebhookResponse(WebhookRequest webhookRequest,String prompt,Object[] contexts,Usecase usecase) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        if(!customer.isPresent()) {
            prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest()) + Emoji.Smiley + ", but before we do that, what is your Ecocash number?";
            OutputContext outputContext = OutputContext.builder()
                    .lifespanCount(5)
                    .name(webhookRequest.getSession()+"/contexts/awaiting_verify_msisdn")
                    .parameters(UnverifiedCustomerParameter.builder().usecase(usecase).build())
                    .build();
            contexts = new Object[]{outputContext};
        }
        return WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .outputContexts(contexts)
                .build();
    }

    private Object[] createTicket(WebhookRequest webhookRequest, Usecase usecase) {
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        if(customer.isPresent())
        {
            Ticket ticket = new Ticket();
            ticket.setProfile(profileRepository.getByChatId(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest())).orElseThrow(() -> new ProfileNotFoundException(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()))));
            ticket.setStatus(Status.OPEN);
            if (webhookRequest.getQueryResult().getSentimentAnalysisResult() != null)
                ticket.setSentimentStart(webhookRequest.getQueryResult().getSentimentAnalysisResult().getQueryTextSentiment().getScore());
            else ticket.setSentimentStart(0.0);
            ticket.setOriginalQueryText(webhookRequest.getQueryResult().getQueryText());
            ticket.setUsecase(usecase);
            ticketRepository.save(ticket);
            TicketParameter ticketParameter = TicketParameter.builder()
                    .id(ticket.getId())
                    .build();
            OutputContext outputContext = OutputContext.builder()
                    .lifespanCount(50)
                    .name(webhookRequest.getSession() + "/contexts/ticket")
                    .parameters(ticketParameter)
                    .build();
            return new Object[]{outputContext};
        }else return null;
    }


}