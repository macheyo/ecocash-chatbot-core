package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.common.MobileNumberFormater;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.PaymentGatewayProcessor;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.data.*;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data.*;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.EipService;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.EipTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.Merchant;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.MerchantRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.SubscriberToMerchantRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.email.EmailService;
import zw.co.cassavasmartech.ecocashchatbotcore.email.data.EmailNotification;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.PromptNotFoundException;
import zw.co.cassavasmartech.ecocashchatbotcore.model.*;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.Function;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.FunctionAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Currency;
import zw.co.cassavasmartech.ecocashchatbotcore.model.emoji.Emoji;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.CustomerRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.ProfileRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.PromptRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.TicketRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.selfservice.SelfServiceCoreProcessor;
import zw.co.cassavasmartech.ecocashchatbotcore.selfservice.data.BandType;
import zw.co.cassavasmartech.ecocashchatbotcore.selfservice.data.EcocashTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.selfservice.data.ReversalApproval;
import zw.co.cassavasmartech.ecocashchatbotcore.selfservice.data.ReversalDto;
import zw.co.cassavasmartech.ecocashchatbotcore.service.CustomerService;
import zw.co.cassavasmartech.ecocashchatbotcore.service.ProfileService;
import zw.co.cassavasmartech.ecocashchatbotcore.service.TicketService;
import zw.co.cassavasmartech.ecocashchatbotcore.sms.SmsService;
import zw.co.cassavasmartech.ecocashchatbotcore.statementservice.StatementServiceConfigurationProperties;
import zw.co.cassavasmartech.ecocashchatbotcore.telegram.TelegramService;
import zw.co.cassavasmartech.ecocashchatbotcore.ussd.UssdPushService;
import zw.co.cassavasmartech.ecocashchatbotcore.ussd.data.PushType;
import zw.co.cassavasmartech.ecocashchatbotcore.ussd.data.UssdPushRequest;

import javax.annotation.PostConstruct;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    PasswordEncoder passwordEncodr;
    private static PasswordEncoder passwordEncoder;


    @Autowired
    TicketRepository ticketRepo;
    private static TicketRepository ticketRepository;

    @Autowired
    TicketService ticketServ;
    private static TicketService ticketService;

    @Autowired
    TelegramService telegramServ;
    private static TelegramService telegramService;

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

    @Autowired
    UssdPushService ussdPushServ;
    private static UssdPushService ussdPushService;

    @Autowired
    StatementServiceConfigurationProperties statementServiceConfig;
    private static StatementServiceConfigurationProperties statementServiceConfigurationProperties;

    @Autowired
    SmsService smsServ;
    private static SmsService smsService;

    @Autowired
    EmailService emailServ;
    private static EmailService emailService;

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
        this.statementServiceConfigurationProperties = statementServiceConfig;
        this.telegramService = telegramServ;
        this.passwordEncoder = passwordEncodr;
        this.emailService = emailServ;
        this.smsService = smsServ;
        this.ussdPushService = ussdPushServ;
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
        if(map.containsKey("source"))return Platform.TELEGRAM;
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
            String name = map.get("name").toString();
            if(name.equalsIgnoreCase(webhookRequest.getSession()+"/contexts/ticket")) ticketIndex=i;
        }
        return objectMapper.convertValue(outputContexts.get(ticketIndex).getParameters(),Map.class);
    }

    public static Map<String,Object> getRecursion(WebhookRequest webhookRequest){
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        ObjectMapper objectMapper = new ObjectMapper();
        int recursionIndex=0;
        for(int i = 0;i<outputContexts.size();i++){
            Map<String,Object> map = objectMapper.convertValue(outputContexts.get(i),Map.class);
            if(map.get("name").toString().equalsIgnoreCase(webhookRequest.getSession()+"/contexts/recursion")) recursionIndex=i;
        }
        return objectMapper.convertValue(outputContexts.get(recursionIndex).getParameters(),Map.class);
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

    public static String[] getBeneficiaryDetails(WebhookRequest webhookRequest){
        Map<String, Object> ticket = DialogFlowUtil.getTicket(webhookRequest);
        TransactionResponse transactionResponse = customerService.customerLookup(SubscriberDto.builder().msisdn(ticket.get("msisdn").toString()).build());
        if(transactionResponse.getField1().equalsIgnoreCase("200"))
            return new String[]{ticket.get("msisdn").toString(),transactionResponse.getField6(),transactionResponse.getField9()};
        Customer customer = isNewCustomer(webhookRequest);
        return new String[]{ticket.get("msisdn").toString(),customer.getFirstName(),customer.getLastName()};
    }

    public static String getCustomerNameAndSurnameByMsisdn(String msisdn){
        TransactionResponse transactionResponse = customerService.customerLookup(SubscriberDto.builder().msisdn(msisdn).build());
        return transactionResponse.getField6()+" "+transactionResponse.getField9();
    }

    public static String[] getMerchantDetails(String msisdn){
        TransactionResponse transactionResponse = paymentGatewayProcessor.lookupMerchant(MerchantLookupRequest.builder().merchant(msisdn).build());

//        Optional<Merchant> merchant = merchantRepository.findByNameOrMerchantCode(msisdn,msisdn);
        if(transactionResponse.getField1().equalsIgnoreCase("200"))
            return new String[]{transactionResponse.getField6(),msisdn};
        return null;
    }


    public static String buyAirtime(WebhookRequest webhookRequest) {
        Customer customer = isNewCustomer(webhookRequest);
        Map<String, Object> ticket = getTicket(webhookRequest);
        Map<String, Object> recursion = getRecursion(webhookRequest);
        ObjectMapper objectMapper = new ObjectMapper();
        String msisdn;
        if(ticket.get("msisdn").toString().equalsIgnoreCase(""))msisdn=customer.getMsisdn();
        else msisdn = ticket.get("msisdn").toString();
        if(recursion.get("intent").toString().equalsIgnoreCase("usecase_send_airtime_scenario1"))
            return paymentGatewayProcessor.subscriberAirtime(SubscriberAirtimeRequest.builder()
                .msisdn1(customer.getMsisdn())
                .msisdn2(msisdn)
                .amount(BigDecimal.valueOf(Double.parseDouble(ticket.get("amount").toString())))
                .ticketId(Double.valueOf(ticket.get("id").toString()).longValue())
                .build()).getField1();
        else {
            Map<String,Object> payment = objectMapper.convertValue(ticket.get("payment"),Map.class);
            if (payment.containsKey("amount"))
                return paymentGatewayProcessor.subscriberAirtime(SubscriberAirtimeRequest.builder()
                        .msisdn1(customer.getMsisdn())
                        .msisdn2(msisdn)
                        .amount(BigDecimal.valueOf(Double.parseDouble(payment.get("amount").toString())))
                        .ticketId(Double.valueOf(ticket.get("id").toString()).longValue())
                        .build()).getField1();
        }
        return null;
    }

    public static String payBill(WebhookRequest  webhookRequest){
        Customer customer = isNewCustomer(webhookRequest);
        Map<String, Object> ticket = getTicket(webhookRequest);
        Map<String, Object> recursion = getRecursion(webhookRequest);
        ObjectMapper objectMapper = new ObjectMapper();
        if(recursion.get("intent").toString().equalsIgnoreCase("usecase_pay_biller_scenario1"))
            return paymentGatewayProcessor.subscriberToBiller(SubscriberToBillerRequest.builder()
                .msisdn(customer.getMsisdn())
                .billerCode(ticket.get("biller.original").toString())
                .amount(BigDecimal.valueOf(Double.parseDouble(ticket.get("amount").toString())))
                .msisdn2(ticket.get("number.original").toString())
                .ticketId(Double.valueOf(ticket.get("id").toString()).longValue())
                .build()).getField1();
        else {
            Map<String,Object> payment = objectMapper.convertValue(ticket.get("payment"),Map.class);
            if (payment.containsKey("amount"))
                return paymentGatewayProcessor.subscriberToBiller(SubscriberToBillerRequest.builder()
                        .msisdn(customer.getMsisdn())
                        .billerCode(ticket.get("biller.original").toString())
                        .amount(BigDecimal.valueOf(Double.parseDouble(payment.get("amount").toString())))
                        .msisdn2(ticket.get("number.original").toString())
                        .ticketId(Double.valueOf(ticket.get("id").toString()).longValue())
                        .build()).getField1();
        }
        return null;

    }


    public static void sendEmail(WebhookRequest webhookRequest){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        Customer customer = isNewCustomer(webhookRequest);
        emailService.send(EmailNotification.builder()
                .sender("no-reply@ecocashholdings.co.zw")
                .subject("Ecocash Chatbot Unfulfilled customer request")
                .to("nunurai@ecocashholdings.co.zw")
                .body("Hello, <br/><br/>Please assist Ecocash customer " + customer.getFirstName()+" "+customer.getLastName() +"("+customer.getMsisdn()+"). "+"<br/>The bot failed to fulfill customer's request under the " + webhookRequest.getQueryResult().getIntent().getDisplayName()+" intent on " + formatter.format(date)+"<br/><br/> Regards<br/><br/> Ecocash Chatbot")
                .build());

    }

    public static String sendMoney(WebhookRequest webhookRequest) {
        Customer customer = isNewCustomer(webhookRequest);
        Map<String, Object> ticket = getTicket(webhookRequest);
        Map<String, Object> recursion = getRecursion(webhookRequest);
        ObjectMapper objectMapper = new ObjectMapper();
        if(recursion.get("intent").toString().equalsIgnoreCase("usecase_send_money_scenario1"))
            return paymentGatewayProcessor.subscriberToSubscriber(SubscriberToSubscriberRequest.builder()
                    .msisdn1(customer.getMsisdn())
                    .msisdn2(ticket.get("msisdn.original").toString())
                    .amount(ticket.get("amount").toString())
                    .ticketId(Double.valueOf(ticket.get("id").toString()).longValue())
                    .build()).getField1();
        else {
            Map<String,Object> payment = objectMapper.convertValue(ticket.get("payment"),Map.class);
            if (payment.containsKey("amount"))
                return paymentGatewayProcessor.subscriberToSubscriber(SubscriberToSubscriberRequest.builder()
                        .msisdn1(customer.getMsisdn())
                        .msisdn2(ticket.get("msisdn.original").toString())
                        .amount(payment.get("amount").toString())
                        .ticketId(Double.valueOf(ticket.get("id").toString()).longValue())
                        .build()).getField1();
        }
        return null;
    }



    public static List<Answer> getAnswerByMsisdnAndAnswerStatus(WebhookRequest webhookRequest){
        Customer customer = isNewCustomer(webhookRequest);
        return selfServiceCoreProcessor.getAnswerByMsisdnAndAnswerStatus(customer.getMsisdn());
    }

    public static String encodeText(String text){
        return passwordEncoder.encode(text);
    }

    public static boolean isEncodingMatch(Answer answer, String userInput){
        log.info("This is encoded customer answer {} and encoded answer from DB {}", userInput, answer.getAnswer());
        return passwordEncoder.matches(userInput,answer.getAnswer());
    }

    public static boolean resetPIN(WebhookRequest webhookRequest){
        boolean response = customerService.pinReset(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()));
        log.info("This is the response from PIN reset service {}", response);
        return response;
    }

    public static void getStatementScene3(WebhookRequest webhookRequest) throws ParseException{
        Customer customer = isNewCustomer(webhookRequest);
        Map<String, Object> ticket = getTicket(webhookRequest);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> datePeriod = objectMapper.convertValue(ticket.get("datePeriod"),Map.class);
        Statement statement = customerService.getStatement(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()), StatementRequest.builder()
                .endDate(datePeriod.get("startDate").toString())
                .startDate(datePeriod.get("endDate").toString())
                .msisdn(customer.getMsisdn())
                .currency(Currency.RTGS)
                .mime(MIME.PDF)
                .encryptDocument(false)
                .passKey("1234")
                .build());
        if(statement!=null) {
            String downloadURL = statement.getFileDownloadUri().replace("http://192.168.92.94:6060/reports-admin/",statementServiceConfigurationProperties.getNgrokServiceEndpointUrl());
            if(getPlatform(webhookRequest.getOriginalDetectIntentRequest()).equals(Platform.TELEGRAM)) {
                telegramService.sendDocument(getChatId(webhookRequest.getOriginalDetectIntentRequest()), downloadURL);
                //return true;
            }
            log.info("Downloaded file: {}",downloadURL);

//            return true;
        }

        //prompt = "Done"+Emoji.Smiley+Emoji.ThumbsUp+"Here you go, please click the link below to download"+Emoji.PointDown+"\n"+statement.getFileDownloadUri().replace("http://217.15.118.15",statementServiceConfigurationProperties.getNgrokServiceEndpointUrl())+"\n\nIs there anything else I can assist you with?"+Emoji.Smiley;
//        return false;

    }

    public static String[] getStatementConfirmationDetails(WebhookRequest webhookRequest){
        Customer customer = isNewCustomer(webhookRequest);
        Map<String, Object> ticket = getTicket(webhookRequest);
        log.info("This is the ticket id: {}", ticket.get("id").toString());
        Ticket ticketObject = ticketService.findById(Double.valueOf(ticket.get("id").toString()).longValue());
        if(ticket.containsKey("startDateTime")&&ticket.containsKey("endDateTime")) {
            ticketObject.setFolio(ticket.get("startDateTime").toString()+" "+ticket.get("endDateTime").toString());
            ticketRepository.save(ticketObject);
            String[] dates=null;
            try {
                dates = correctStatementDate(ticket.get("startDateTime").toString(),ticket.get("endDateTime").toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return dates;
        }
        else {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String,Object> datePeriod = objectMapper.convertValue(ticket.get("datePeriod"),Map.class);
            ticketObject.setFolio(datePeriod.get("startDate").toString()+" "+datePeriod.get("endDate").toString());
            ticketRepository.save(ticketObject);
            String[] dates=null;
            try {
                dates = correctStatementDate(datePeriod.get("startDate").toString(),datePeriod.get("endDate").toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return dates;
        }
    }

    public static String[] correctStatementDate(String startDateTime, String endDateTime) throws ParseException {
        DateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+02:00");
        DateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        log.info("These are the switched dates: {} and {}", switcharoo(startDateTime), switcharoo(endDateTime));
        Date startDate = isoFormat.parse(startDateTime);
        Date endDate = isoFormat.parse(endDateTime);
        if(startDate.getYear()>today.getYear())startDate.setYear(today.getYear());
        if(endDate.getTime()>today.getYear())endDate.setYear(today.getYear());
        return new String[]{newFormat.format(startDate),newFormat.format(endDate)};
    }

    public static String switcharoo(String date){
        String d = date.substring(0,9);
        String dt[] = d.split("-");
        return dt[0]+"-"+dt[2]+"-"+dt[1]+date.substring(10);
    }

    public static void getStatement(String startDate, String endDate, Profile profile) throws ParseException {
        String password = String.valueOf(new Random().nextInt(900000) + 100000);
        Statement statement = customerService.getStatement(profile.getChatId(), StatementRequest.builder()
                .endDate(startDate)
                .startDate(endDate)
                .msisdn(profile.getCustomer().getMsisdn())
                .currency(Currency.RTGS)
                .mime(MIME.PDF)
                .encryptDocument(true)
                .passKey(password)
                .build());
        if(statement!=null) {
            String downloadURL = statement.getFileDownloadUri().replace("http://192.168.92.94:6060/reports-admin/",statementServiceConfigurationProperties.getNgrokServiceEndpointUrl());
            smsService.sendSms(profile.getCustomer().getMsisdn(),password,"messages.statement.password");
            if(profile.getPlatform().equals(Platform.TELEGRAM)) {
                telegramService.sendDocument(profile.getChatId(), downloadURL);
            }
            log.info("Downloaded file: {}",downloadURL);
        }

            //prompt = "Done"+Emoji.Smiley+Emoji.ThumbsUp+"Here you go, please click the link below to download"+Emoji.PointDown+"\n"+statement.getFileDownloadUri().replace("http://217.15.118.15",statementServiceConfigurationProperties.getNgrokServiceEndpointUrl())+"\n\nIs there anything else I can assist you with?"+Emoji.Smiley;
//        return false;
    }

    public static String downloadFromUrl(URL url, String localFilename) throws IOException {
        InputStream is = null;
        FileOutputStream fos = null;
        String tempDir = System.getProperty("java.io.tmpdir");
        String outputPath = tempDir + "/" + localFilename;
        try {
            URLConnection urlConn = url.openConnection();
            is = urlConn.getInputStream();
            fos = new FileOutputStream(outputPath);
            byte[] buffer = new byte[4096];
            int length;
            while ((length = is.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            return outputPath;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        }
    }

    public static EipTransaction payForStatement(WebhookRequest webhookRequest) {
        Customer customer = isNewCustomer(webhookRequest);
        Map<String, Object> ticket = getTicket(webhookRequest);
        String merchantName = paymentGatewayProcessor.lookupMerchant(MerchantLookupRequest.builder().merchant(statementServiceConfigurationProperties.getMerchantMsisdn()).build()).getField6();
        String merchantMsisdn = paymentGatewayProcessor.lookupMerchant(MerchantLookupRequest.builder().merchant(statementServiceConfigurationProperties.getMerchantMsisdn()).build()).getField10();
//        return paymentGatewayProcessor.subscriberToMerchant(zw.co.cassavasmartech.ecocashchatbotcore.cpg.data.SubscriberToMerchantRequest.builder()
//                    .subscriberMsisdn(customer.getMsisdn())
//                    .merchantMsisdn(merchantMsisdn)
//                    .merchantName(merchantName)
//                    .amount(BigDecimal.valueOf(Double.valueOf(statementServiceConfigurationProperties.getChargeAmount())))
//                    .ticketId(Double.valueOf(ticket.get("id").toString()).longValue())
//                    .build()).getField1();
        return eipService.postPayment(zw.co.cassavasmartech.ecocashchatbotcore.eip.data.SubscriberToMerchantRequest.builder().msisdn(customer.getMsisdn())
                .msisdn(customer.getMsisdn())
                .merchantCode(statementServiceConfigurationProperties.getMerchantMsisdn())
                .amount(BigDecimal.valueOf(Double.valueOf(statementServiceConfigurationProperties.getChargeAmount())))
                .ticketId(Double.valueOf(ticket.get("id").toString()).longValue())
                .build());
    }

    public static EipTransaction payMerchant(WebhookRequest webhookRequest){
        Customer customer = isNewCustomer(webhookRequest);
        Map<String, Object> ticket = getTicket(webhookRequest);
        Map<String, Object> recursion = getRecursion(webhookRequest);
        ObjectMapper objectMapper = new ObjectMapper();
        String merchantName = paymentGatewayProcessor.lookupMerchant(MerchantLookupRequest.builder().merchant(ticket.get("msisdn.original").toString()).build()).getField6();
        String merchantMsisdn = paymentGatewayProcessor.lookupMerchant(MerchantLookupRequest.builder().merchant(ticket.get("msisdn.original").toString()).build()).getField10();

        if(recursion.get("intent").toString().equalsIgnoreCase("usecase_pay_merchant_scenario1"))
//            return paymentGatewayProcessor.subscriberToMerchant(zw.co.cassavasmartech.ecocashchatbotcore.cpg.data.SubscriberToMerchantRequest.builder()
//                    .subscriberMsisdn(customer.getMsisdn())
//                    .merchantName(merchantName)
//                    .merchantMsisdn(merchantMsisdn)
//                    .amount(BigDecimal.valueOf(Double.parseDouble(ticket.get("amount").toString())))
//                    .ticketId(Double.valueOf(ticket.get("id").toString()).longValue())
//                    .build()).getField1();
        {
            EipTransaction eipTransaction = eipService.postPayment(SubscriberToMerchantRequest.builder()
                    .msisdn(customer.getMsisdn())
                    .merchantMsisdn(merchantMsisdn)
                    .merchantName(merchantName)
                    .merchantCode(ticket.get("msisdn.original").toString())
                    .amount(BigDecimal.valueOf(Double.parseDouble(ticket.get("amount").toString())))
                    .ticketId(Double.valueOf(ticket.get("id").toString()).longValue())
                    .build());
            log.info("Eip transaction: {}", eipTransaction);
            return eipTransaction;
        } else {
            Map<String,Object> payment = objectMapper.convertValue(ticket.get("payment"),Map.class);
            if (payment.containsKey("amount"))
                return eipService.postPayment(zw.co.cassavasmartech.ecocashchatbotcore.eip.data.SubscriberToMerchantRequest.builder().msisdn(customer.getMsisdn())
                        .msisdn(customer.getMsisdn())
                        .merchantCode(ticket.get("msisdn.original").toString())
                        .amount(BigDecimal.valueOf(Double.parseDouble(ticket.get("amount").toString())))
                        .ticketId(Double.valueOf(ticket.get("id").toString()).longValue())
                        .build());
//                return paymentGatewayProcessor.subscriberToMerchant(zw.co.cassavasmartech.ecocashchatbotcore.cpg.data.SubscriberToMerchantRequest.builder()
//                        .subscriberMsisdn(customer.getMsisdn())
//                        .merchantMsisdn(ticket.get("msisdn.original").toString())
//                        .merchantName(merchantName)
//                        .amount(BigDecimal.valueOf(Double.parseDouble(payment.get("amount").toString())))
//                        .ticketId(Double.valueOf(ticket.get("id").toString()).longValue())
//                        .build()).getField1();
        }
        return null;
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
            if (webhookRequest.getQueryResult().getSentimentAnalysisResult() != null)
                ticket.setSentimentEnd(webhookRequest.getQueryResult().getSentimentAnalysisResult().getQueryTextSentiment().getScore());
            else ticket.setSentimentEnd(0.0);
            ticket.setTicketStatus(ticketStatus);
            if(map.containsKey("id"))
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
        String[] sessionInfo = webhookRequest.getSession().split("/");
        ticket.setConversationId(sessionInfo[sessionInfo.length-1]);
        ticketRepository.save(ticket);
        RecursionParameter recursionParameter = RecursionParameter.builder().intent(webhookRequest.getQueryResult().getIntent().getDisplayName()).iteration(0).build();
        TicketParameter ticketParameter = TicketParameter.builder().id(ticket.getId()).usecase(usecase).build();
        OutputContext ticketContext = OutputContext.builder().lifespanCount(50).name(webhookRequest.getSession() + "/contexts/ticket").parameters(ticketParameter).build();
        OutputContext recursionContext = OutputContext.builder().lifespanCount(5).name(webhookRequest.getSession() + "/contexts/recursion").parameters(recursionParameter).build();
        return new Object[]{ticketContext,recursionContext};
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
                case TRANSACTION_REVERSAL:contextToBeRemoved="/awaiting_reversal_choice";
                break;
                case MERCHANT_PAYMENT:contextToBeRemoved="/awaiting_merchant_msisdn";
                break;
                case SEND_MONEY:contextToBeRemoved="/awaiting_send_money_msisdn";
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
//        int iteration;
//        Map<String,Object> recursionObject = getRecursion(webhookRequest);
//        if(recursionObject.get("intent").toString().equalsIgnoreCase(webhookRequest.getQueryResult().getIntent().getDisplayName())){
//            int i = Integer.parseInt(recursionObject.get("iteration").toString());
//            if(i>1) prompt="So here we are, at the end of the road....";
//            else {
//                iteration = i++;
//            }
//        }else{
//
//        }
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

    public static boolean isMerchantValid(WebhookRequest webhookRequest){
        String response = paymentGatewayProcessor.lookupMerchant(MerchantLookupRequest.builder().merchant(webhookRequest.getQueryResult().getQueryText()).build()).getField1();
        if(response.equalsIgnoreCase("200"))return true;
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

    public static String[] getTransactionDetails(WebhookRequest webhookRequest){
        Customer customer = isNewCustomer(webhookRequest);
        Map<String,Object> ticket = getTicket(webhookRequest);
        HttpEntity<ApiResponse<Optional<EcocashTransaction>>> response  = selfServiceCoreProcessor.validateReversal(customer.getMsisdn(),ticket.get("ecocashReference.original").toString());
        if(response.getBody().getBody().isPresent() && response.getBody().getStatus()== HttpStatus.OK.value()) {
            TransactionResponse transactionResponse = customerService.customerLookup(SubscriberDto.builder().msisdn(response.getBody().getBody().get().getRecipientMobileNumber()).build());
            String transactionReference = response.getBody().getBody().get().getTransactionReference();
            String transactionDate = response.getBody().getBody().get().getTransactionDate().toString();
            String recipientMobileNumber = response.getBody().getBody().get().getRecipientMobileNumber();
            String recipient = transactionResponse.getField6() + " " + transactionResponse.getField9();
            String transactionAmount = "$RTGS" + response.getBody().getBody().get().getAmount();
            return new String[]{transactionReference,transactionDate,recipientMobileNumber,recipient,transactionAmount};
        }
        else return null;

    }

    public static String[] getPendingTransactionDetails(WebhookRequest webhookRequest){
        Customer customer = isNewCustomer(webhookRequest);
        Map<String,Object> ticket = getTicket(webhookRequest);
        List<ReversalDto> reversals = selfServiceCoreProcessor.pendingReversals(customer.getMsisdn()).getBody().getBody();
        String[] reversalDetails = new String[]{};
        for(ReversalDto reversal:reversals){
            if(reversal.getReference().equalsIgnoreCase(ticket.get("ecocashReference.original").toString())) {
                reversalDetails = new String[]{
                        reversal.getReference(),
                        reversal.getOriginalSenderMobileNumber(),
                        getCustomerNameAndSurnameByMsisdn(reversal.getOriginalSenderMobileNumber()),
                        reversal.getAmount().toString()};
            }
        }
        return reversalDetails;
    }

    public static HttpEntity<ApiResponse<Optional<ReversalDto>>> initiateTransactionReversal(WebhookRequest webhookRequest){

        Customer customer = isNewCustomer(webhookRequest);
        Map<String,Object> ticket = getTicket(webhookRequest);
        return selfServiceCoreProcessor.initiateReversal(customer.getMsisdn(),ticket.get("ecocashReference.original").toString());
    }

    public static Boolean validateCustomerPIN(WebhookRequest webhookRequest){
        Map<String,Object> ticket = getTicket(webhookRequest);
        Customer customer = isNewCustomer(webhookRequest);
        Ticket ticketObject = ticketService.findById(Double.valueOf(ticket.get("id").toString()).longValue());
        ticketObject.setReference(mobileNumberFormater.formatMobileNumberInternational(customer.getMsisdn()));
        ticketObject.setFolio(ticket.get("ecocashReference.original").toString());
        ticketService.updateSingle(ticketObject);
        return ussdPushService.sendPrompt(UssdPushRequest.builder()
                .message("You have a pending reversal. Please enter your EcoCash PIN to authorize")
                .serviceCode("903*600")
                .pushType(PushType.PROMPT)
                .msisdn(customer.getMsisdn())
                .build());
    }

    public static HttpEntity<ApiResponse<Optional<ReversalDto>>> approveTransactionReversal(Ticket ticket){
        Customer customer = ticket.getProfile().getCustomer();
        List<ReversalDto> reversals = selfServiceCoreProcessor.pendingReversals(customer.getMsisdn()).getBody().getBody();
        ReversalDto reversalDto = null;
        for(ReversalDto reversal:reversals)
            if(reversal.getReference().equalsIgnoreCase(ticket.getFolio()))
                reversalDto=reversal;
        ticket.setTicketStatus(TicketStatus.CLOSED);
        ticketService.updateSingle(ticket);
        return selfServiceCoreProcessor.approveReversal(ReversalApproval.builder().reversalId(reversalDto.getId()).bandType(BandType.SELF_INITIATED).applyCharge(true).build());
    }

    public static List<ReversalDto> getPendingReversals(WebhookRequest webhookRequest){
        Customer customer = isNewCustomer(webhookRequest);
        return selfServiceCoreProcessor.pendingReversals(customer.getMsisdn()).getBody().getBody();
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
                prompt += "Are you buying airtime for yourself or for someone else?";
                OutputContext buyAirtimeOutputContext = OutputContext.builder()
                        .lifespanCount(1)
                        .name(webhookRequest.getSession()+"/contexts/awaiting_send_airtime_choice")
                        .build();
                return WebhookResponse.builder()
                        .fulfillmentText(prompt)
                        .source("ecocashchatbotcore")
                        .outputContexts(new Object[]{buyAirtimeOutputContext,createTicket(webhookRequest, UseCase.BUY_AIRTIME)[0]})
                        .build();
            case SEND_MONEY:
                prompt += "What is the EcoCash number you want to send money to?";
                OutputContext sendMoneyOutputContext = OutputContext.builder()
                        .lifespanCount(1)
                        .name(webhookRequest.getSession()+"/contexts/awaiting_send_money_msisdn")
                        .build();
                return WebhookResponse.builder()
                        .fulfillmentText(prompt)
                        .source("ecocashchatbotcore")
                        .outputContexts(new Object[]{sendMoneyOutputContext,createTicket(webhookRequest, UseCase.BUY_AIRTIME)[0]})
                        .build();
            case MERCHANT_PAYMENT:
                prompt += "What is the Merchant code/ name of merchant you want to pay?";
                OutputContext merchantPaymentOutputContext = OutputContext.builder()
                        .lifespanCount(1)
                        .name(webhookRequest.getSession()+"/contexts/awaiting_merchant_msisdn")
                        .build();
                return WebhookResponse.builder()
                        .fulfillmentText(prompt)
                        .source("ecocashchatbotcore")
                        .outputContexts(new Object[]{merchantPaymentOutputContext,createTicket(webhookRequest, UseCase.BUY_AIRTIME)[0]})
                        .build();
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
                prompt+= "Do you want to reverse a transaction or approve a reversal request?";
                OutputContext transactionReversalOutputContext = OutputContext.builder()
                        .lifespanCount(1)
                        .name(webhookRequest.getSession()+"/contexts/awaiting_reversal_choice")
                        .build();
                return WebhookResponse.builder()
                        .fulfillmentText(prompt)
                        .source("ecocashchatbotcore")
                        .outputContexts(new Object[]{transactionReversalOutputContext,createTicket(webhookRequest, UseCase.TRANSACTION_REVERSAL)[0]})
                        .build();
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
        if(selfServiceCoreProcessor.getAnswerByMsisdnAndAnswerStatus(customer.getMsisdn()).size()>0) return true;
        else return false;
    }
}
