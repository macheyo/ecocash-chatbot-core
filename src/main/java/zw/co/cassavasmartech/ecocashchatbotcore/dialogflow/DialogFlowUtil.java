package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.ecocashchatbotcore.common.MobileNumberFormater;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.PaymentGatewayProcessor;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.data.BillerLookupRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.data.SubscriberAirtimeRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.data.SubscriberToBillerRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.*;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.EipService;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.EipTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.Merchant;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.MerchantRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.SubscriberToMerchantRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.PromptNotFoundException;
import zw.co.cassavasmartech.ecocashchatbotcore.model.*;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.Function;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.FunctionAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.emoji.Emoji;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.CustomerRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.ProfileRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.PromptRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.TicketRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.selfServiceCore.SelfServiceCoreProcessor;
import zw.co.cassavasmartech.ecocashchatbotcore.service.CustomerService;
import zw.co.cassavasmartech.ecocashchatbotcore.service.ProfileService;
import zw.co.cassavasmartech.ecocashchatbotcore.service.TicketService;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
@Component
public class DialogFlowUtil {

    @Autowired
    ProfileRepository profileRepo;
    private static ProfileRepository profileRepository;

    @Autowired
    CustomerRepository customerRepo;
    private static CustomerRepository customerRepository;

    @Autowired
    TicketRepository ticketRepo;
    private static TicketRepository ticketRepository;

    @Autowired
    TicketService ticketServ;
    private static TicketService ticketService;

    @Autowired
    SelfServiceCoreProcessor selfServiceCore;
    private static SelfServiceCoreProcessor selfServiceCoreProcessor;

    @Autowired
    PromptRepository promptRepo;
    private static PromptRepository promptRepository;

    @Autowired
    MobileNumberFormater mobNumberFormater;
    private static MobileNumberFormater mobileNumberFormater;

    @Autowired
    CustomerService customerServ;
    private static CustomerService customerService;

    @Autowired
    ProfileService profileServ;
    private static ProfileService profileService;

    @Autowired
    PaymentGatewayProcessor paymentGatewayProc;
    private static PaymentGatewayProcessor paymentGatewayProcessor;

    @Autowired
    MerchantRepository merchantRepo;
    private static MerchantRepository merchantRepository;

    @Autowired
    EipService eipServ;
    private static EipService eipService;


    @PostConstruct
    public void init() {
        this.profileRepository = profileRepo;
        this.customerRepository = customerRepo;
        this.ticketRepository = ticketRepo;
        this.selfServiceCoreProcessor = selfServiceCore;
        this.promptRepository = promptRepo;
        this.mobileNumberFormater = mobNumberFormater;
        this.customerService = customerServ;
        this.profileService = profileServ;
        this.paymentGatewayProcessor = paymentGatewayProc;
        this.ticketService = ticketServ;
        this.merchantRepository = merchantRepo;
        this.eipService = eipServ;
    }

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

    public static String getPlatformName(OriginalDetectIntentRequest originalDetectIntentRequest, Optional<Customer> newCustomer) {
        return null;
    }

    public static Customer isNewCustomer(WebhookRequest webhookRequest){
        Optional<Profile> profile = profileRepository.getByChatId(getChatId(webhookRequest.getOriginalDetectIntentRequest()));
        if(profile.isPresent()&&profile.get().isVerified()) {
            return customerRepository.findByProfilesChatId(getChatId(webhookRequest.getOriginalDetectIntentRequest())).get();
        }else
            return null;
    }

    public static Map<String,Object> getTicket(WebhookRequest webhookRequest){
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        ObjectMapper objectMapper = new ObjectMapper();
        int ticketIndex=0;
        for(int i = 0;i<outputContexts.size();i++){
            Map<String,Object> map = objectMapper.convertValue(outputContexts.get(i),Map.class);
            if(map.get("name").toString().equalsIgnoreCase(webhookRequest.getSession()+"/contexts/ticket")) ticketIndex=i;
        }
        return objectMapper.convertValue(outputContexts.get(ticketIndex).getParameters(),Map.class);
    }

    public static Map<String,Object> getUsecase(WebhookRequest webhookRequest){
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        ObjectMapper objectMapper = new ObjectMapper();
        int usecaseIndex=0;
        for(int i = 0;i<outputContexts.size();i++){
            Map<String,Object> map = objectMapper.convertValue(outputContexts.get(i),Map.class);
            if(map.get("name").toString().equalsIgnoreCase(webhookRequest.getSession()+"/contexts/awaiting_verify_msisdn")) usecaseIndex=i;
        }
        return objectMapper.convertValue(outputContexts.get(usecaseIndex).getParameters(),Map.class);
    }

    public static String getBillProviderDetails(String billerCode){
        BillerLookupRequest billerLookupRequest = new BillerLookupRequest();
        billerLookupRequest.setBiller(billerCode);
        return paymentGatewayProcessor.lookupBiller(billerLookupRequest).getField6();

    }

    public static String[] getBeneficiaryDetails(String msisdn){
        TransactionResponse transactionResponse = customerService.customerLookup(SubscriberDto.builder().msisdn(msisdn).build());
        if(transactionResponse.getField1().equalsIgnoreCase("200"))
            return new String[]{transactionResponse.getField6(),transactionResponse.getField9()};
        return null;
    }

    public static String[] getMerchantDetails(String msisdn){
        Optional<Merchant> merchant = merchantRepository.findByNameOrMerchantCode(msisdn,msisdn);
        if(merchant.isPresent())
        return new String[]{merchant.get().getName(),merchant.get().getMerchantCode()};
        return null;
    }


    public static String buyAirtime(WebhookRequest webhookRequest) {
        Customer customer = isNewCustomer(webhookRequest);
        Map<String, Object> ticket = getTicket(webhookRequest);
        return paymentGatewayProcessor.subscriberAirtime(SubscriberAirtimeRequest.builder()
                .msisdn1(customer.getMsisdn())
                .msisdn2(ticket.get("msisdn").toString())
                .amount(BigDecimal.valueOf(Double.parseDouble(ticket.get("amount").toString())))
                .ticketId(Double.valueOf(ticket.get("id").toString()).longValue())
                .build()).getField1();
    }

    public static String payBill(WebhookRequest  webhookRequest){
        Customer customer = isNewCustomer(webhookRequest);
        Map<String, Object> ticket = getTicket(webhookRequest);
        return paymentGatewayProcessor.subscriberToBiller(SubscriberToBillerRequest.builder()
                .msisdn(customer.getMsisdn())
                .billerCode(ticket.get("biller.original").toString())
                .amount(BigDecimal.valueOf(Double.parseDouble(ticket.get("amount").toString())))
                .msisdn2(ticket.get("number.original").toString())
                .ticketId(Double.valueOf(ticket.get("id").toString()).longValue())
                .build()).getField1();

    }

    public static EipTransaction payMerchant(WebhookRequest webhookRequest){
        Customer customer = isNewCustomer(webhookRequest);
        Map<String, Object> ticket = getTicket(webhookRequest);
        return eipService.postPayment(SubscriberToMerchantRequest.builder().msisdn(customer.getMsisdn())
                .msisdn(customer.getMsisdn())
                .merchantCode(getMerchantDetails(ticket.get("msisdn.original").toString())[1])
                .amount(BigDecimal.valueOf(Double.parseDouble(ticket.get("amount").toString())))
                .ticketId(Double.valueOf(ticket.get("id").toString()).longValue())
                .build());
    }

    public static Object[] closeTicket(WebhookRequest webhookRequest, TicketStatus ticketStatus){
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        ObjectMapper objectMapper = new ObjectMapper();
        int ticketIndex=0;
        for(int i = 0;i<outputContexts.size();i++){
            Map<String,Object> map = objectMapper.convertValue(outputContexts.get(i),Map.class);
            if(map.get("name").toString().equalsIgnoreCase(webhookRequest.getSession()+"/contexts/ticket")) ticketIndex=i;
        }
        OutputContext outputContext=null;
        Map<String,Object> map = objectMapper.convertValue(outputContexts.get(ticketIndex).getParameters(),Map.class);
        Customer customer = isNewCustomer(webhookRequest);
        if(customer!=null) {
            Ticket ticket = new Ticket();
            ticket.setStage(4);
            ticket.setSentimentEnd(webhookRequest.getQueryResult().getSentimentAnalysisResult().getQueryTextSentiment().getScore());
            ticket.setTicketStatus(ticketStatus);
            ticketService.update(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()),Double.valueOf(map.get("id").toString()).longValue(),ticket);
            outputContext = OutputContext.builder()
                    .lifespanCount(0)
                    .name(webhookRequest.getSession() + "/contexts/ticket")
                    .build();
        }
        return new Object[]{outputContext};
    }

    public static Object[] createTicket(WebhookRequest webhookRequest, UseCase usecase) {
        Ticket ticket = new Ticket();
        Optional<Profile> profile = profileRepository.getByChatId(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()));
        if(profile.isPresent()&&profile.get().isVerified()==true) ticket.setProfile(profile.get());
        else return null;
        ticket.setTicketStatus(TicketStatus.OPEN);
        if (webhookRequest.getQueryResult().getSentimentAnalysisResult() != null)
            ticket.setSentimentStart(webhookRequest.getQueryResult().getSentimentAnalysisResult().getQueryTextSentiment().getScore());
        else ticket.setSentimentStart(0.0);
        ticket.setOriginalQueryText(webhookRequest.getQueryResult().getQueryText());
        ticket.setUsecase(usecase);
        ticketRepository.save(ticket);
        TicketParameter ticketParameter = TicketParameter.builder()
                .id(ticket.getId())
                .usecase(usecase)
                .build();
        OutputContext outputContext = OutputContext.builder()
                .lifespanCount(50)
                .name(webhookRequest.getSession() + "/contexts/ticket")
                .parameters(ticketParameter)
                .build();
        return new Object[]{outputContext};
    }

    public static String promptProcessor(int position, WebhookRequest webhookRequest, Customer customer){
        Optional<Prompt> prompt = Optional.ofNullable(promptRepository.findByIntentAndPosition(webhookRequest.getQueryResult().getIntent().getDisplayName(), position).orElseThrow(() -> new PromptNotFoundException()));
        String[] phrase = prompt.get().getText().split("\\s");
        String processed="";
        for(String word:phrase){
            FunctionAdapter fa = Function.lookup(word);
            if(fa!=null) {
                if(!fa.hasArgs()) processed +=" "+fa.process();
                else {
                    PromptObject args = PromptObject.builder().webhookRequest(webhookRequest).customer(customer).build();
                    processed +=" "+fa.process(args);
                }
            }
            else processed+=" "+word;
        }
        return processed;
    }

    public static WebhookResponse getResponse(WebhookRequest webhookRequest, String prompt, Object[] contexts, UseCase usecase) {
        Customer customer = isNewCustomer(webhookRequest);
        if(customer==null) {
            String contextToBeRemoved="/redundant";
            switch (usecase){
                case PIN_RESET:contextToBeRemoved="/awaiting_pinreset_enrollment_confirmation";
                break;
                case BILL_PAYMENT:contextToBeRemoved="/awaiting_biller_code";
                break;
            }
            OutputContext redundantContext = OutputContext.builder()
                    .lifespanCount(0)
                    .name(webhookRequest.getSession()+"/contexts"+contextToBeRemoved)
                    .build();
            OutputContext goForVerification = OutputContext.builder()
                    .lifespanCount(5)
                    .name(webhookRequest.getSession()+"/contexts/awaiting_verify_msisdn")
                    .parameters(UnverifiedCustomerParameter.builder().usecase(usecase).build())
                    .build();
            if(!usecase.equals(UseCase.WELCOME)) {
                if(contexts==null) contexts = new Object[]{goForVerification, redundantContext};
            }
            else contexts = new Object[]{};
        }
        WebhookResponse response = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .outputContexts(contexts)
                .build();
        log.info("Processing response to dialogflow: {}", response);
        return response;
    }

    public static boolean isBillerCodeValid(WebhookRequest webhookRequest){
        BillerLookupRequest billerLookupRequest = new BillerLookupRequest();
        billerLookupRequest.setBiller(webhookRequest.getQueryResult().getQueryText());
        String response = paymentGatewayProcessor.lookupBiller(billerLookupRequest).getField1();
        if(response.equalsIgnoreCase("200"))return true;
        else return false;
    }

    public static boolean isCustomerValid(WebhookRequest webhookRequest) {
        String response = paymentGatewayProcessor.lookupCustomer(webhookRequest.getQueryResult().getQueryText()).getField1();
        if(response.equalsIgnoreCase("200"))return true;
        return false;
    }

    public static boolean isMerchantNameOrCodeValid(WebhookRequest webhookRequest){
        Optional<Merchant> merchant = merchantRepository.findByNameOrMerchantCode(webhookRequest.getQueryResult().getQueryText(),webhookRequest.getQueryResult().getQueryText());
        if(merchant.isPresent()) return true;
        return false;
    }

    public static WebhookResponse defaultUnknownCustomerResponse(WebhookRequest webhookRequest){
        return WebhookResponse.builder()
                .fulfillmentText("Before we proceed I need to verify your identity...What is your EcoCash number?")
                .source("ecocashchatbot")
                .outputContexts(new Object[]{OutputContext.builder()
                        .lifespanCount(1)
                        .name(webhookRequest.getSession()+"/contexts/awaiting_verify_msisdn")
                        .build()})
                .build();
    }

    public static WebhookResponse resumeConversation(WebhookRequest webhookRequest) {
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        ObjectMapper objectMapper = new ObjectMapper();
        int usecaseIndex=0;
        for(int i = 0;i<outputContexts.size();i++){
            Map<String,Object> map = objectMapper.convertValue(outputContexts.get(i),Map.class);
            if(map.get("name").toString().equalsIgnoreCase(webhookRequest.getSession()+"/contexts/awaiting_verify_msisdn")) usecaseIndex=i;
        }
        Map<String,Object> map = objectMapper.convertValue(outputContexts.get(usecaseIndex).getParameters(),Map.class);
        String prompt = "Great! you have been verified" + Emoji.ThumbsUp + Emoji.Grin + " Now that we have that out of the way...\n";
        switch (UseCase.valueOf(map.get("usecase").toString()) ){
            case PIN_RESET:
                if (isEnrolled(customerService.getByChatId(getChatId(webhookRequest.getOriginalDetectIntentRequest())))) {
                    prompt+= "I can see you are enrolled on our self service platform \uD83D\uDE01 may I go on to ask your security questions to verify your identity?";
                }
                else prompt+= "You are currently not enrolled on our self service platform  \uD83D\uDE27. An agent is going to call you in a few minutes to verify your identity and reset your PIN";
                OutputContext pinresetOutputContext = OutputContext.builder()
                        .lifespanCount(1)
                        .name(webhookRequest.getSession()+"/contexts/awaiting_pinreset_enrollment_confirmation")
                        .build();
                return WebhookResponse.builder()
                        .fulfillmentText(prompt)
                        .source("ecocashchatbotcore")
                        .outputContexts(new Object[]{pinresetOutputContext,createTicket(webhookRequest, UseCase.PIN_RESET)[0]})
                        .build();
            case BUY_AIRTIME:
                break;
            case SEND_MONEY:
                break;
            case MERCHANT_PAYMENT:
                break;
            case BILL_PAYMENT:
                prompt+= "What is the biller code that you want pay?";
                OutputContext billpaymentOutputContext = OutputContext.builder()
                        .lifespanCount(1)
                        .name(webhookRequest.getSession()+"/contexts/awaiting_biller_code")
                        .build();
                return WebhookResponse.builder()
                        .fulfillmentText(prompt)
                        .source("ecocashchatbotcore")
                        .outputContexts(new Object[]{billpaymentOutputContext,createTicket(webhookRequest, UseCase.BILL_PAYMENT)[0]})
                        .build();
            case SUBSCRIBER_STATEMENT:
                prompt += "on what date"+Emoji.Calendar+" do you want the statement to start from?";
                OutputContext outputContext = OutputContext.builder()
                        .lifespanCount(1)
                        .name(webhookRequest.getSession()+"/contexts/awaiting_start_date")
                        .build();
                return WebhookResponse.builder()
                        .fulfillmentText(prompt)
                        .source("ecocashchatbotcore")
                        .outputContexts(new Object[]{outputContext,createTicket(webhookRequest, UseCase.SUBSCRIBER_STATEMENT)[0]})
                        .build();
            case REGISTRATION:
                break;
            case SEND_MONEY_TARIFF:
                break;
            case BILL_TARIFF:
                break;
            case MERCHANT_TARIFF:
                break;
            case BILL_PROVIDER_LOOKUP:
                break;
            case MERCHANT_LOOKUP:
                break;
            case TRANSACTION_REVERSAL:
                break;
            case WELCOME:
                break;
            case VERIFICATION:
                break;
        }

        return null;
    }

    public static String getMinimumMsisdn(String msisdn){
        return mobileNumberFormater.formatMsisdnMinimum(msisdn);
    }

    public static Optional<Customer> findCustomerByMsisdn(String msisdn){
        return customerService.findByMsisdn(msisdn);
    }

    public static boolean verifyCustomer(String chatid, String verificationCode){
        return profileService.verifyCustomer(chatid,verificationCode);
    }

    public static void saveCustomer(Customer customer){
        customerService.save(customer);
    }

    public static TransactionResponse customerLookup(SubscriberDto subscriberDto){
        return customerService.customerLookup(subscriberDto);
    }

    public static Optional<Profile> findCustomerProfileByChatId(String chatid){
        return profileService.getByChatId(chatid);
    }

    public static void saveProfile(String msisdn, Profile profile){
        profileService.save(msisdn, profile);
    }

    public static void generateOTP(String chatid){
        profileService.generateOtp(chatid);
    }

    public static boolean isEnrolled(Customer customer){
        if(selfServiceCoreProcessor.isEnrolled(customer.getMsisdn()).getIsEnrolled().equalsIgnoreCase("true")) return true;
        else return false;
    }
}
