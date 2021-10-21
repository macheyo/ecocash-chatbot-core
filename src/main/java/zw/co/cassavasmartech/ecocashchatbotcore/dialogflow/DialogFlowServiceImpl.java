package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
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
import zw.co.cassavasmartech.ecocashchatbotcore.model.emoji.Emoji;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.Function;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.function.FunctionAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UseCase;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.Intent;
import zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.intent.IntentHandlerAdapter;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.CustomerRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.ProfileRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.TicketRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.selfservice.data.EcocashTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.selfservice.SelfServiceCoreProcessor;
import zw.co.cassavasmartech.ecocashchatbotcore.selfservice.data.ReversalDto;
import zw.co.cassavasmartech.ecocashchatbotcore.service.CustomerService;
import zw.co.cassavasmartech.ecocashchatbotcore.service.ProfileService;
import zw.co.cassavasmartech.ecocashchatbotcore.service.PromptService;
import zw.co.cassavasmartech.ecocashchatbotcore.service.TicketService;
import zw.co.cassavasmartech.ecocashchatbotcore.statementservice.StatementServiceConfigurationProperties;

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
    TicketService ticketService;
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
//    @Autowired
//    PasswordEncoder passwordEncoder;
    @Autowired
    PromptService promptService;

    @Override
    public WebhookResponse processWebhookCall(WebhookRequest webhookRequest) throws ParseException {
        log.info("Processing dialogflow transaction request {}\n", webhookRequest);
        IntentHandlerAdapter intentHandlerAdapter = Intent.lookup(webhookRequest.getQueryResult().getIntent().getDisplayName());
            if(intentHandlerAdapter!=null)
                return intentHandlerAdapter.getWebhookResponse(webhookRequest);
            else return null;



//        log.info("Processing dialogflow transaction request {}\n", webhookRequest);
//        switch (webhookRequest.getQueryResult().getIntent().getDisplayName()) {
//            case "Default Welcome Intent":
//                return welcomeHandler(webhookRequest);
//            case "usecase.pinreset":
//                return pinresetUsecaseHandler(webhookRequest);
//            case "usecase.pinreset.security.questions.affirmative":
//                return pinresetUsecaseSecurityQuestionsAffirmativeHandler(webhookRequest);
//            case "usecase.pinreset.security.questions.fallback":
//                return pinresetUsecaseSecurityQuestionsFallbackHandler(webhookRequest);
//            case "usecase.pinreset.security.questions.first.answer":
//                return pinresetUsecaseSecurityQuestionsFirstAnswerHandler(webhookRequest);
//            case "usecase.pinreset.security.questions.more.negative":
//                return pinresetUsecaseSecurityQuestionsMoreNegativeHandler(webhookRequest);
//            case "usecase.pinreset.security.questions.more.affirmative":
//                return pinresetUsecaseSecurityQuestionsMoreAffirmativeHandler(webhookRequest);
//            case "usecase.pinreset.security.questions.more.fallback":
//                return pinresetUsecaseSecurityQuestionsMoreFallbackHandler(webhookRequest);
//            case "usecase.pay.biller.scenario1":
//                return payBillerUsecaseScenario1Handler(webhookRequest);
//            case "usecase.pay.biller.scenario2":
//                return payBillerUsecaseScenario2Handler(webhookRequest);
//            case "usecase.pay.biller.get.biller.amount":
//                return payBillerUsecaseGetBillerAmountHandler(webhookRequest);
//            case "usecase.pay.biller.get.biller.amount.fallback":
//                return payBillerUsecaseGetBillerAmountFallbackHandler(webhookRequest);
//            case "usecase.pay.biller.get.biller.code":
//                return payBillerUsecaseGetBillerCodeHandler(webhookRequest);
//            case "usecase.pay.biller.get.biller.code.fallback":
//                return payBillerUsecaseGetBillerCodeFallbackHandler(webhookRequest);
//            case "usecase.pay.biller.get.account.intent":
//                return payBillerUsecaseGetBillerAccountHandler(webhookRequest);
//            case "usecase.pay.biller.get.account.intent.fallback":
//                return payBillerUsecaseGetBillerAccountFallbackHandler(webhookRequest);
//            case "usecase.pay.biller.get.biller.confirmation.affirmative":
//                return payBillerUsecaseGetBillerConfirmationAffirmativeHandler(webhookRequest);
//            case "usecase.pay.biller.get.biller.confirmation.negative":
//                return payBillerUsecaseGetBillerConfirmationNegativeHandler(webhookRequest);
//            case "usecase.pay.biller.get.biller.confirmation.fallback":
//                return payBillerUsecaseGetBillerConfirmationFallbackHandler(webhookRequest);
//            case "usecase.pay.biller.confirmation.negative - yes":
//                return payBillerUsecaseGetBillerConfirmationNegativeAffirmHandler(webhookRequest);
//            case "usecase.pay.biller.confirmation.negative - no":
//                return payBillerUsecaseGetBillerConfirmationNegativeNegHandler(webhookRequest);
//            case "usecase.pay.biller.more.affirmative":
//                return payBillerUsecaseMoreAffirmativeHandler(webhookRequest);
//            case "usecase.pay.biller.more.negative":
//                return payBillerUsecaseMoreNegativeHandler(webhookRequest);
//            case "usecase.pay.biller.more.fallback":
//                return payBillerUsecaseMoreFallbackHandler(webhookRequest);
//            case "usecase.statement.scenario.1":
//                return statementUsecaseScenario1Handler(webhookRequest);
//            case "usecase.statement.scenario.2":
//                return statementUsecaseScenario2Handler(webhookRequest);
//            case "usecase.statement.scenario.3":
//                return statementUsecaseScenario3Handler(webhookRequest);
//            case "usecase.statement.end.date":
//                return statementUsecaseEndDateHandler(webhookRequest);
//            case "usecase.statement.start.date":
//                return statementUsecaseStartDateHandler(webhookRequest);
//            case "usecase.statement.start.date.fallback":
//                return statementUsecaseStartDateFallbackHandler(webhookRequest);
//            case "usecase.statement.end.date.fallback":
//                return statementUsecaseEndDateFallbackHandler(webhookRequest);
//            case "usecase.statement.more.affirmative":
//                return statementUsecaseMoreAffirmativeHandler(webhookRequest);
//            case "usecase.statement.more.negative":
//                return statementUsecaseMoreNegativeHandler(webhookRequest);
//            case "usecase.statement.more.fallback":
//                return statementUsecaseMoreFallbackHandler(webhookRequest);
//            case "usecase.pay.merchant.scenario1":
//                return payMerchantUsecaseScenario1Handler(webhookRequest);
//            case "usecase.pay.merchant.scenario2":
//                return payMerchantUsecaseScenario2Handler(webhookRequest);
//            case "usecase.pay.merchant.get.msisdn":
//                return payMerchantUsecaseGetMsisdnOrNameHandler(webhookRequest);
//            case "usecase.pay.merchant.get.msisdn.fallback":
//                return payMerchantUsecaseGetMsisdnOrNameFallbackHandler(webhookRequest);
//            case "usecase.pay.merchant.get.name":
//                return payMerchantUsecaseGetMsisdnOrNameHandler(webhookRequest);
//            case "usecase.pay.merchant.get.amount":
//                return payMerchantUsecaseGetAmountHandler(webhookRequest);
//            case "usecase.pay.merchant.get.amount.fallback":
//                return payMerchantUsecaseGetAmountFallbackHandler(webhookRequest);
//            case "usecase.pay.merchant.confirmation.affirmative":
//                return payMerchantUsecaseConfirmationAffirmativeHandler(webhookRequest);
//            case "usecase.pay.merchant.confirmation.fallback":
//                return payMerchantUsecaseConfirmationFallbackHandler(webhookRequest);
//            case "usecase.pay.merchant.confirmation.negative":
//                return payMerchantUsecaseConfirmationNegativeHandler(webhookRequest);
//            case "usecase.pay.merchant.confirmation.negative - yes":
//                return payMerchantUsecaseConfirmationNegativeAffirmHandler(webhookRequest);
//            case "usecase.pay.merchant.confirmation.negative - no":
//                return payMerchantUsecaseConfirmationNegativeNegHandler(webhookRequest);
//            case "usecase.pay.merchant.more.affirmative":
//                return payMerchantUsecaseMoreAffirmativeHandler(webhookRequest);
//            case "usecase.pay.merchant.more.negative":
//                return payMerchantUsecaseMoreNegativeHandler(webhookRequest);
//            case "usecase.pay.merchant.more.fallback":
//                return payMerchantUsecaseMoreFallbackHandler(webhookRequest);
//            case "usecase.send.airtime.scenario1":
//                return sendAirtimeUsecaseScenario1Handler(webhookRequest);
//            case "usecase.send.airtime.scenario2":
//                return sendAirtimeUsecaseScenario2Handler(webhookRequest);
//            case "usecase.send.airtime.myself":
//                return sendAirtimeUsecaseMyselfHandler(webhookRequest);
//            case "usecase.send.airtime.myself.fallback":
//                return sendAirtimeUsecaseMyselfFallbackHandler(webhookRequest);
//            case "usecase.send.airtime.myself.negative":
//                return sendAirtimeUsecaseMyselfNegativeHandler(webhookRequest);
//            case "usecase.send.airtime.get.beneficiary.msisdn":
//                return sendAirtimeUsecaseGetBeneficiaryHandler(webhookRequest);
//            case "usecase.send.airtime.get.beneficiary.msisdn.fallback":
//                return sendAirtimeUsecaseGetBeneficiaryFallbackHandler(webhookRequest);
//            case "usecase.send.airtime.get.beneficiary.amount":
//                return sendAirtimeUsecaseGetAmountHandler(webhookRequest);
//            case "usecase.send.airtime.get.beneficiary.amount.fallback":
//                return sendAirtimeUsecaseGetAmountFallbackHandler(webhookRequest);
//            case "usecase.send.airtime.confirmation.affirmative":
//                return sendAirtimeUsecaseConfirmationAffirmativeHandler(webhookRequest);
//            case "usecase.send.airtime.confirmation.fallback":
//                return sendAirtimeUsecaseConfirmationFallbackHandler(webhookRequest);
//            case "usecase.send.airtime.more.affirmative":
//                return sendAirtimeUsecaseMoreAffirmativeHandler(webhookRequest);
//            case "usecase.send.airtime.more.negative":
//                return sendAirtimeUsecaseMoreNegativeHandler(webhookRequest);
//            case "usecase.send.airtime.more.fallback":
//                return sendAirtimeUsecaseMoreFallbackHandler(webhookRequest);
//            case "usecase.send.money.scenario1":
//                return sendMoneyUsecaseScenario1Handler(webhookRequest);
//            case "usecase.send.money.scenario2":
//                return sendMoneyUsecaseScenario2Handler(webhookRequest);
//            case "usecase.send.money.get.beneficiary.msisdn":
//                return sendMoneyUsecaseGetBeneficiaryMsisdnHandler(webhookRequest);
//            case "usecase.send.money.get.beneficiary.amount":
//                return sendMoneyUsecaseGetBeneficiaryAmountHandler(webhookRequest);
//            case "usecase.send.money.confirmation.affirmative":
//                return sendMoneyUsecaseConfimationAffirmativeHandler(webhookRequest);
//            case "usecase.verify.get.msisdn":
//                return verifyUsecaseGetMsisdnHandler(webhookRequest);
//            case "usecase.verify.get.otp":
//                return verifyUsecaseGetOtpHandler(webhookRequest);
//            case "usecase.send.money.get.beneficiary.msisdn.fallback":
//                return sendMoneyUsecaseMsisdnFallbackHandler(webhookRequest);
//            case "usecase.send.money.get.beneficiary.amount.fallback":
//                return sendMoneyUsecaseAmountFallbackHandler(webhookRequest);
//            case"intentSendMoneyTariff":
//                return sendMoneyTarrifUsecaseHandler(webhookRequest);
//            case "usecase.transaction.reversal.intent":
//                return transactionReversalUsecaseHandler(webhookRequest);
//            case "usecase.transaction.reversal.reverse":
//                return transactionReversalUsecaseReverseHandler(webhookRequest);
//            case "usecase.transaction.reversal.reverse.reference":
//                return transactionReversalUsecaseReverseReferenceHandler(webhookRequest);
//            case "usecase.transaction.reversal.reverse.affirmative":
//                return transactionReversalReverseAffirmativeHandler(webhookRequest);
//            case "usecase.transaction.reversal.approve":
//                return transactionReversalApproveHandler(webhookRequest);
//            case "usecase.transaction.reversal.approve.reference":
//                return transactionReversalUsecaseApproveReferenceHandler(webhookRequest);
//
////            case "usecase.transaction.reversal.approval.affirmative":
////                return transactionReversalUsecaseApprovalAffirmativeHandler(webhookRequest);
////            case "usecase.transaction.reversal.approval.confirmation.fallback":
////                return transactionReversalUsecaseApprovalConfirmationFallbackHandler(webhookRequest);
////            case "usecase.transaction.reversal.approval.negative":
////                return transactionReversalUsecaseApprovalNegativeHandler(webhookRequest);
////            case "usecase.transaction.reversal.approval.reference":
////                return transactionReversalUsecaseApprovalReferenceHandler(webhookRequest);
////            case "usecase.transaction.reversal.approval.reference.fallback":
////                return transactionReversalUsecaseApprovalReferenceFallbackHandler(webhookRequest);
//
////            case "usecase.transaction.reversal.fallback":
////                return transactionReversalUsecaseFallbackHandler(webhookRequest);
////            case "usecase.transaction.reversal.more.affirmative":
////                return transactionReversalUsecaseMoreAffirmativeHandler(webhookRequest);
////            case "usecase.transaction.reversal.more.fallback":
////                return transactionReversalUsecaseMoreFallbackHandler(webhookRequest);
////            case "usecase.transaction.reversal.more.negative":
////                return transactionReversalUsecaseMoreNegativeHandler(webhookRequest);
////
//
////            case "usecase.transaction.reversal.reverse.confirmation.fallback":
////                return transactionReversalReversalReverseConfirmationHandler(webhookRequest);
////            case "usecase.transaction.reversal.reverse.negative":
////                return transactionReversalReverseNegativeHandler(webhookRequest);
////            case "usecase.transaction.reversal.reverse.reference":
////                return transactionReversalUsecaseReverseReferenceHandler(webhookRequest);
////            case "usecase.transaction.reversal.reverse.reference.fallback":
////                return transactionReversalUsecaseReverseReferenceFallbackHandler(webhookRequest);
//
//
//        }
//        return null;
    }

    private WebhookResponse transactionReversalUsecaseApproveReferenceHandler(WebhookRequest webhookRequest) {
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
            HttpEntity<ApiResponse<Optional<EcocashTransaction>>> response  = selfServiceCoreProcessor.validateReversal(customer.get().getMsisdn(),map.get("ecocashReference").toString());
            if(response.getBody().getStatus()!= HttpStatus.OK.value())prompt="Ooops"+ Emoji.Pensive+response.getBody().getMessage();
            else {
                TransactionResponse transactionResponse = customerService.customerLookup(SubscriberDto.builder().msisdn(response.getBody().getBody().get().getRecipientMobileNumber()).build());
                prompt = "Got it!!" + Emoji.Smiley + "\ncan you confirm you want to approve reversal for transaction "
                        + response.getBody().getBody().get().getTransactionReference()
                        + " done on "
                        + response.getBody().getBody().get().getTransactionDate()
                        + " to "
                        + response.getBody().getBody().get().getRecipientMobileNumber()
                        +"("+transactionResponse.getField6()+" "+transactionResponse.getField9()+")"
                        +" for $ZWL"+response.getBody().getBody().get().getAmount()
                        +"\nis this "+Emoji.PointUp+" correct?";
            }
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+Emoji.Smiley+", but before we reverse, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse transactionReversalApproveHandler(WebhookRequest webhookRequest) {
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
            HttpEntity<ApiResponse<List<ReversalDto>>> reversals  = selfServiceCoreProcessor.pendingReversals(customer.get().getMsisdn());
            if(reversals.getBody().getStatus()!= HttpStatus.OK.value())prompt="Ooops"+Emoji.Pensive+reversals.getBody().getMessage();
            else {
                int count = 0;
                if(reversals.getBody().getBody().size()>0)
                {
                    prompt = Emoji.PointDown + " These are reversals waiting for your approval:";
                    for (ReversalDto reversal : reversals.getBody().getBody()) {
                        count++;
                        TransactionResponse transactionResponse = customerService.customerLookup(SubscriberDto.builder().msisdn(reversal.getOriginalSenderMobileNumber()).build());
                        prompt += "\n" + count + ". "
                                + reversal.getReference() + " "
                                + reversal.getOriginalSenderMobileNumber() + " "
                                + "(" + transactionResponse.getField6() + " " + transactionResponse.getField9() + ")"
                                + " of $ZWL" + reversal.getAmount();
                    }
                    prompt += "\nwhat is the reference of the transaction you want to approve reversal for?";
                }else prompt = "You have no pending reversals. Anything else I can do for you?";
            }
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+Emoji.Smiley+", but before we reverse, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse transactionReversalReverseAffirmativeHandler(WebhookRequest webhookRequest) {
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
            HttpEntity<ApiResponse<Optional<ReversalDto>>> response  = selfServiceCoreProcessor.initiateReversal(customer.get().getMsisdn(),map.get("ecocashReference").toString());
            if(response.getBody().getStatus()!= HttpStatus.OK.value()) {
                prompt = "Ooops" + Emoji.Pensive + response.getBody().getMessage() + "\nAnything else I can do for you?";
            }
            else {
                TransactionResponse transactionResponse = customerService.customerLookup(SubscriberDto.builder().msisdn(response.getBody().getBody().get().getOriginalRecipientMobileNumber()).build());
                prompt = "Done!!" + Emoji.Smiley + "\nreversal of transaction "
                        + response.getBody().getBody().get().getReference()
                        + " to "
                        + response.getBody().getBody().get().getOriginalRecipientMobileNumber()
                        +"("+transactionResponse.getField6()+" "+transactionResponse.getField9()+")"
                        +" for $ZWL"+response.getBody().getBody().get().getAmount()
                        +"\nhas been initiated"+Emoji.ThumbsUp+". A notification has been sent to "+transactionResponse.getField6()+" for approval. Anything else I can do for you?";
            }
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+Emoji.Smiley+", but before we reverse, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse transactionReversalUsecaseReverseReferenceHandler(WebhookRequest webhookRequest) {
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
            HttpEntity<ApiResponse<Optional<EcocashTransaction>>> response  = selfServiceCoreProcessor.validateReversal(customer.get().getMsisdn(),map.get("ecocashReference").toString());
            if(response.getBody().getStatus()!= HttpStatus.OK.value())prompt="Ooops"+Emoji.Pensive+response.getBody().getMessage();
            else {
                TransactionResponse transactionResponse = customerService.customerLookup(SubscriberDto.builder().msisdn(response.getBody().getBody().get().getRecipientMobileNumber()).build());
                prompt = "Got it!!" + Emoji.Smiley + "\ncan you confirm you want to reverse transaction "
                        + response.getBody().getBody().get().getTransactionReference()
                        + " done on "
                        + response.getBody().getBody().get().getTransactionDate()
                        + " to "
                        + response.getBody().getBody().get().getRecipientMobileNumber()
                        +"("+transactionResponse.getField6()+" "+transactionResponse.getField9()+")"
                        +" for $ZWL"+response.getBody().getBody().get().getAmount()
                        +"\nis this "+Emoji.PointUp+" correct?";
            }
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+Emoji.Smiley+", but before we reverse, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse transactionReversalUsecaseReverseHandler(WebhookRequest webhookRequest) {
        String prompt = "Great!!"+Emoji.Smiley+" \nWhat is the transaction reference of the transaction you want to reverse?";
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.TRANSACTION_REVERSAL);
    }

    private WebhookResponse transactionReversalUsecaseHandler(WebhookRequest webhookRequest) {
        String prompt = "Alright, lets quickly do that"+Emoji.Smiley+" \nDo you want to reverse a transaction or approve a reversal?";
        return getWebhookResponse(webhookRequest,prompt,createTicket(webhookRequest, UseCase.TRANSACTION_REVERSAL), UseCase.TRANSACTION_REVERSAL);
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
        String prompt;
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
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+Emoji.Smiley+", but before we buy your airtime, what is your Ecocash number?";
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
        String prompt;
        if(customer.isPresent()) {
            prompt = "Alright thats fine "+DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+","+Emoji.Smiley+"\nSo in summary you want to send $ZWL"+ map.get("amount").toString()+" to "+map.get("msisdn").toString()+" ("+transactionResponse.getField6()+" "+transactionResponse.getField9()+")\ncan you confirm this is correct?";
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+Emoji.Smiley+", but before we pay sen your money, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse sendMoneyUsecaseGetBeneficiaryMsisdnHandler(WebhookRequest webhookRequest) {
        String prompt = "Got it"+Emoji.Smiley+"How much do you want to send?";
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.SEND_MONEY);
    }

    private WebhookResponse sendMoneyUsecaseScenario2Handler(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> map = objectMapper.convertValue(outputContexts.get(0).getParameters(),Map.class);
        Map<String,Object> payment = objectMapper.convertValue(map.get("payment"),Map.class);
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        TransactionResponse transactionResponse = customerService.customerLookup(SubscriberDto.builder().msisdn(map.get("msisdn").toString()).build());
        String prompt;
        if(customer.isPresent()) {
            prompt = "Alright thats fine "+DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+","+Emoji.Smiley+"\nSo in summary you want to send $ZWL"+ payment.get("amount").toString()+" to "+map.get("msisdn").toString()+" ("+transactionResponse.getField6()+" "+transactionResponse.getField9()+")\ncan you confirm this is correct?";
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+Emoji.Smiley+", but before we buy your airtime, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .outputContexts(createTicket(webhookRequest, UseCase.SEND_MONEY))
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse sendMoneyUsecaseScenario1Handler(WebhookRequest webhookRequest) {
        String prompt = "Alright, lets quickly do that"+Emoji.Smiley+" what is the Ecocash number you want to send money to?";
        return getWebhookResponse(webhookRequest,prompt,createTicket(webhookRequest, UseCase.SEND_MONEY), UseCase.SEND_MONEY);
    }

    private WebhookResponse verifyUsecaseGetOtpHandler(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        if(profileService.verifyCustomer(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()),webhookRequest.getQueryResult().getQueryText())) {
            return resumeConversation(webhookRequest);
        }
        else {
            return WebhookResponse.builder()
                    .fulfillmentText("Unfortunately I have failed to verify you. The verification code you entered is either expired or invalid...")
                    .source("ecocashchatbotcore")
                    .build();
        }

    }

    private WebhookResponse verifyUsecaseGetMsisdnHandler(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        Customer customer = new Customer();
        customer.setMsisdn(mobileNumberFormater.formatMsisdnMinimum(webhookRequest.getQueryResult().getQueryText()));
        Optional<Customer> c = customerRepository.findByMsisdn(mobileNumberFormater.formatMsisdnMinimum(webhookRequest.getQueryResult().getQueryText()));
        if(!c.isPresent()) {
            customerService.save(customer);
        }
        String prompt;
        if(customerService.customerLookup(SubscriberDto.builder().msisdn(mobileNumberFormater.formatMsisdnMinimum(webhookRequest.getQueryResult().getQueryText())).build()).getField1().equalsIgnoreCase("200")){
            Optional<Customer> newCustomer = customerRepository.findByMsisdn(mobileNumberFormater.formatMsisdnMinimum(webhookRequest.getQueryResult().getQueryText()));
            if(!profileRepository.getByChatId(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest())).isPresent()) {
                Profile profile = new Profile();
                profile.setAlias(DialogFlowUtil.getPlatformName(webhookRequest.getOriginalDetectIntentRequest(), newCustomer));
                profile.setChatId(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()));
                profile.setPlatform(DialogFlowUtil.getPlatform(webhookRequest.getOriginalDetectIntentRequest()));
                profileService.save(newCustomer.get().getMsisdn(), profile);
                prompt = "Great! got it" + Emoji.ThumbsUp + "\nI have sent a verification code via sms to your Ecocash number, enter that code here to verify your identity. \nThe code is only valid for 2 minutes" + Emoji.Exclamation;
                profileService.generateOtp(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()));

            }else{
                prompt = "Great! got it" + Emoji.ThumbsUp + "\nI have sent a verification code via sms to your Ecocash number, enter that code here to verify your identity. \nThe code is only valid for 2 minutes" + Emoji.Exclamation;
                profileService.generateOtp(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()));
            }
        }else prompt = "Unfortunately, this number "+Emoji.PointUp+"is not registered on Ecocash "+Emoji.Disappointed+" \nCheck again, is the number "+webhookRequest.getQueryResult().getQueryText()+" correct?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse sendAirtimeUsecaseMoreAffirmativeHandler(WebhookRequest webhookRequest) {
        String prompt = "Ok great. What else can I help you do?"+Emoji.Smiley;
        return getWebhookResponse(webhookRequest,prompt,closeTicket(webhookRequest, TicketStatus.CLOSED), UseCase.BUY_AIRTIME);
    }

    private WebhookResponse sendAirtimeUsecaseMoreNegativeHandler(WebhookRequest webhookRequest) {
        String prompt = "Ok great. Have a nice day and remember to continue living life the Ecocash way"+Emoji.Smiley;
        return getWebhookResponse(webhookRequest,prompt,closeTicket(webhookRequest, TicketStatus.CLOSED), UseCase.BUY_AIRTIME);
    }

    private WebhookResponse sendAirtimeUsecaseMoreFallbackHandler(WebhookRequest webhookRequest) {
        String prompt = "Do you mean you still need me to help you?."+Emoji.Smiley;
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.BUY_AIRTIME);
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
        String prompt;
        if(customer.isPresent()) {
                TransactionResponse transactionResponse = paymentGatewayProcessor.subscriberAirtime(SubscriberAirtimeRequest.builder()
                                .msisdn1(customer.get().getMsisdn())
                                .msisdn2(map.get("msisdn").toString())
                               // .amount(map.get("amount").toString())
                                .ticketId(Double.valueOf(map.get("id").toString()).longValue()).build());
                if (transactionResponse.getField1().equalsIgnoreCase("603"))
                    prompt = "Great!!" + Emoji.Smiley + "Processing your payment right now.\nYou will receive a prompt on your phone for you to enter your PIN.\n" + Emoji.Exclamation + "You have to enter the correct PIN to complete this transaction";
                else
                    prompt = "Oops!!" + Emoji.Pensive + "Something went wrong with the transaction\nWould you like to try this again?";

        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+Emoji.Smiley+", but before we buy your airtime, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse sendAirtimeUsecaseConfirmationFallbackHandler(WebhookRequest webhookRequest) {
        String prompt = "Does this mean you want to proceed with this transaction"+Emoji.Confused;
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.BUY_AIRTIME);
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
        String prompt;
        if(customer.isPresent()) {
            prompt = "Alright thats fine "+DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+","+Emoji.Smiley+"\nSo in summary you want to buy $ZWL"+ map.get("amount").toString()+" for "+map.get("msisdn").toString()+" ("+transactionResponse.getField6()+" "+transactionResponse.getField9()+")\ncan you confirm this is correct?";
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+Emoji.Smiley+", but before we pay your airtime. what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse sendAirtimeUsecaseGetAmountFallbackHandler(WebhookRequest webhookRequest) {
        String prompt = "Oops that wo'nt work"+Emoji.Pensive+"Enter the amount as a currency e.g., $200";
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.BUY_AIRTIME);
    }

    private WebhookResponse sendAirtimeUsecaseGetBeneficiaryHandler(WebhookRequest webhookRequest) {
        String prompt = "Got it"+Emoji.Smiley+"How much airtime do you want to buy?";
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.BUY_AIRTIME);
    }

    private WebhookResponse sendAirtimeUsecaseGetBeneficiaryFallbackHandler(WebhookRequest webhookRequest) {
        String prompt = "Oops that wo'nt work"+Emoji.Pensive+"Enter the econet number like 0774087642";
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.BUY_AIRTIME);
    }

    private WebhookResponse sendAirtimeUsecaseMyselfNegativeHandler(WebhookRequest webhookRequest) {
        String prompt = "what is the Econet number you want to buy airtime for?";
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.BUY_AIRTIME);
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
        if(customer.isPresent())
        return getWebhookResponse(webhookRequest,prompt,new Object[]{OutputContext.builder()
                .lifespanCount(50)
                .name(webhookRequest.getSession()+"/contexts/ticket")
                .parameters(TicketParameter.builder().id(Double.valueOf(map.get("id").toString()).longValue()).msisdn(customer.get().getMsisdn()).build())
                .build()}, UseCase.BUY_AIRTIME);
        else return getWebhookResponse(webhookRequest,prompt,null, UseCase.BUY_AIRTIME);
    }

    private WebhookResponse sendAirtimeUsecaseMyselfFallbackHandler(WebhookRequest webhookRequest) {
        String prompt = "Do you mean you want to buy airtime for yourself?"+Emoji.Confused;
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.BUY_AIRTIME);
    }

    //send money fallbacks
    private WebhookResponse sendMoneyUsecaseMsisdnFallbackHandler(WebhookRequest webhookRequest){
        String prompt = "You seem to have provided an invalid Econet Number, please try a number of this format: 0774222689";
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.SEND_MONEY);
    }

    private WebhookResponse sendMoneyUsecaseAmountFallbackHandler(WebhookRequest webhookRequest){
        String prompt = "The amount you entered is not supported. Use format like $300";
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.SEND_MONEY);
    }



    private WebhookResponse sendAirtimeUsecaseScenario2Handler(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> map = objectMapper.convertValue(outputContexts.get(0).getParameters(),Map.class);
        Map<String,Object> payment = objectMapper.convertValue(map.get("payment"),Map.class);
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        TransactionResponse transactionResponse = customerService.customerLookup(SubscriberDto.builder().msisdn(map.get("msisdn").toString()).build());
        String prompt;
        if(customer.isPresent()) {
            prompt = "Alright thats fine "+DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+","+Emoji.Smiley+"\nSo in summary you want to buy $ZWL"+ payment.get("amount").toString()+" for "+map.get("msisdn").toString()+" ("+transactionResponse.getField6()+" "+transactionResponse.getField9()+")\ncan you confirm this is correct?";
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+Emoji.Smiley+", but before we buy your airtime, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .outputContexts(createTicket(webhookRequest, UseCase.BUY_AIRTIME))
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse sendAirtimeUsecaseScenario1Handler(WebhookRequest webhookRequest) {
        String prompt = "Alright, lets quickly do that"+Emoji.Smiley+" are you buying airtime for yourself?";
        return getWebhookResponse(webhookRequest,prompt,createTicket(webhookRequest, UseCase.BUY_AIRTIME), UseCase.BUY_AIRTIME);
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
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+Emoji.Smiley+", but before we pay your bill, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse payMerchantUsecaseConfirmationFallbackHandler(WebhookRequest webhookRequest) {
        String prompt = "So does that mean you want to proceed with the payment?"+Emoji.Confused;
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.MERCHANT_PAYMENT);}

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
        String prompt;
        Object[] contexts = null;
        if(customer.isPresent()) {
            if(merchant.isPresent()) prompt = "Alright thats fine,"+Emoji.Smiley+"\nSo in summary you want to pay $ZWL"+ map.get("amount").toString() +" to merchant code "+merchant.get().getMerchantCode()+" ("+merchant.get().getName()+")\ncan you confirm this is correct?";
            else {
                prompt = map.get("msisdn") + " is not a valid Merchant," + Emoji.Pensive + " Please check and retype the Merchant code or Name";
                OutputContext outputContext = OutputContext.builder()
                        .lifespanCount(1)
                        .name(webhookRequest.getSession()+"/contexts/awaiting_merchant_msisdn")
                        .build();
                OutputContext outputContext1 = OutputContext.builder()
                        .lifespanCount(0)
                        .name(webhookRequest.getSession()+"/contexts/awaiting_pay_merchant_onfirmation")
                        .build();
                contexts = new Object[]{outputContext, outputContext1};
            }
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+Emoji.Smiley+", but before we pay your merchant, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .outputContexts(contexts)
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;

    }

    private WebhookResponse payMerchantUsecaseGetAmountFallbackHandler(WebhookRequest webhookRequest) {
        String prompt = "Sorry that won't work"+Emoji.Pensive+" type the amount you want to pay as a currency e.g $4257 or 2345 ";
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.MERCHANT_PAYMENT);
    }

    private WebhookResponse payMerchantUsecaseGetMsisdnOrNameHandler(WebhookRequest webhookRequest) {
        String prompt = "Got it"+Emoji.Smiley+"How much do you want to pay?";
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.MERCHANT_PAYMENT);
    }

    private WebhookResponse payMerchantUsecaseGetMsisdnOrNameFallbackHandler(WebhookRequest webhookRequest) {
        String prompt = "Sorry that won't work"+Emoji.Pensive+" type the merchant code as a number e.g 42578 or merchant name e.g. Escrow";
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.MERCHANT_PAYMENT);
    }

    private WebhookResponse payMerchantUsecaseMoreFallbackHandler(WebhookRequest webhookRequest) {
        String prompt = "Do you mean you still need me to help you?."+Emoji.Smiley;
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.MERCHANT_PAYMENT);
    }

    private WebhookResponse payMerchantUsecaseMoreNegativeHandler(WebhookRequest webhookRequest) {
        String prompt = "Ok great. Have a nice day and remember to continue living life the Ecocash way"+Emoji.Smiley;
        return getWebhookResponse(webhookRequest,prompt,closeTicket(webhookRequest, TicketStatus.CLOSED), UseCase.MERCHANT_PAYMENT);
    }

    private WebhookResponse payMerchantUsecaseMoreAffirmativeHandler(WebhookRequest webhookRequest) {
        String prompt = "Ok great. What else can I help you do?"+Emoji.Smiley;
        return getWebhookResponse(webhookRequest,prompt,closeTicket(webhookRequest, TicketStatus.CLOSED), UseCase.MERCHANT_PAYMENT);
    }

    private WebhookResponse payMerchantUsecaseConfirmationNegativeNegHandler(WebhookRequest webhookRequest) {
        String prompt = "Ok great. Have a nice day and remember to continue living life the Ecocash way"+Emoji.Smiley;
        return getWebhookResponse(webhookRequest,prompt,closeTicket(webhookRequest, TicketStatus.CLOSED), UseCase.MERCHANT_PAYMENT);
    }

    private WebhookResponse payMerchantUsecaseConfirmationNegativeAffirmHandler(WebhookRequest webhookRequest) {
        String prompt = "Ok "+Emoji.Grimacing+" lets do this one more time. Enter the merchant code "+Emoji.PointDown;
        return getWebhookResponse(webhookRequest, prompt, null, UseCase.MERCHANT_PAYMENT);
    }

    private WebhookResponse payMerchantUsecaseConfirmationNegativeHandler(WebhookRequest webhookRequest) {
        String prompt = "That's ok"+ Emoji.Weary+" . Would you want to re-try?";
        return getWebhookResponse(webhookRequest, prompt, null, UseCase.MERCHANT_PAYMENT);
    }

//    private WebhookResponse pinResetUsecaseConfirmationAffirmativeHandler(WebhookRequest webhookRequest){
//        String prompt = "Is this to mean you want to continue and answer your security questions?";
//        return getWebhookResponse(webhookRequest,prompt,null,Usecase.PIN_RESET);
//    }
//
//    private WebhookResponse pinResetUsecaseConfirmationNegativeHandler(WebhookRequest webhookRequest){
//        String prompt = "Ok great. Do you have anything else that I can assist you with?";
//        return getWebhookResponse(webhookRequest, prompt,null,Usecase.PIN_RESET);
//    }
    //send money tariff
    private WebhookResponse sendMoneyTarrifUsecaseHandler(WebhookRequest webhookRequest){
        log.info("Processing dflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        String prompt = "Ok to send that amount you need ...";
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.SEND_MONEY_TARIFF);
    }

    //bill payment tariff
    private WebhookResponse payBillerTariffUsecaseHandler(WebhookRequest webhookRequest){
        log.info("Processing Dflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        String prompt = "To send this to biller you need to have this .......placeholder";
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.BILL_TARIFF);
    }

    //merchant payment tariff
    private WebhookResponse payMerchantTariffUsecaseHandler(WebhookRequest webhookRequest){
        log.info("Processing Dflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        String prompt = "to pay ths merchant u need....";
        return getWebhookResponse(webhookRequest,prompt,createTicket(webhookRequest, UseCase.MERCHANT_TARIFF), UseCase.MERCHANT_TARIFF);
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
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+Emoji.Smiley+", but before we pay your bill, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .outputContexts(createTicket(webhookRequest, UseCase.MERCHANT_PAYMENT))
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse payMerchantUsecaseScenario1Handler(WebhookRequest webhookRequest) {
        String prompt = "Alright, lets quickly do that"+Emoji.Smiley+" what is the Merchant code or Name of the Merchant you want to pay?";
        return getWebhookResponse(webhookRequest,prompt,createTicket(webhookRequest, UseCase.MERCHANT_PAYMENT), UseCase.MERCHANT_PAYMENT);
    }

    private WebhookResponse statementUsecaseStartDateHandler(WebhookRequest webhookRequest) {
        String prompt = "Ok to which date would you want the statement to be generated "+Emoji.Date+"?";
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.SUBSCRIBER_STATEMENT);
    }

    private WebhookResponse statementUsecaseStartDateFallbackHandler(WebhookRequest webhookRequest) {
        String prompt = "Please type in a valid start date, for example 1 January?"+Emoji.PointUp;
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.SUBSCRIBER_STATEMENT);
    }

    private WebhookResponse statementUsecaseEndDateFallbackHandler(WebhookRequest webhookRequest) {
        String prompt = "Please type in a valid end date, for example 31 January?"+Emoji.PointUp;
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.SUBSCRIBER_STATEMENT);
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
                            .mime(MIME.PDF)
                            .build());
            if(statement!=null)
                prompt = "Done"+Emoji.Smiley+Emoji.ThumbsUp+"Here you go, please click the link below to download"+Emoji.PointDown+"\n"+statement.getFileDownloadUri().replace("http://217.15.118.15",statementServiceConfigurationProperties.getNgrokServiceEndpointUrl())+"\n\nIs there anything else I can assist you with?"+Emoji.Smiley;
            else prompt = "Oops!!"+Emoji.Pensive+"Something went wrong with the transaction\nWould you like to try this again?";
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+Emoji.Smiley+", but before we get your statement, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse statementUsecaseMoreFallbackHandler(WebhookRequest webhookRequest) {
        String prompt = "Didn't quite get you, do you still need my help?"+Emoji.Smiley;
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.SUBSCRIBER_STATEMENT);
    }

    private WebhookResponse statementUsecaseMoreAffirmativeHandler(WebhookRequest webhookRequest) {
        String prompt = "Ok Great! What else can I do for you?"+Emoji.Smiley;
        return getWebhookResponse(webhookRequest,prompt,closeTicket(webhookRequest, TicketStatus.CLOSED), UseCase.SUBSCRIBER_STATEMENT);
    }

    private WebhookResponse statementUsecaseMoreNegativeHandler(WebhookRequest webhookRequest) {
        String prompt = "Ok Great! Enjoy the rest of your day. And remember to continue living your life the ecocash way!"+Emoji.Smiley;
        return getWebhookResponse(webhookRequest,prompt,closeTicket(webhookRequest, TicketStatus.CLOSED), UseCase.SUBSCRIBER_STATEMENT);
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
            context = createTicket(webhookRequest, UseCase.SUBSCRIBER_STATEMENT);
            Statement statement = customerService.getStatement(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()), StatementRequest.builder()
                    .endDate(datePeriod.get("startDate").toString())
                    .startDate(datePeriod.get("endDate").toString())
                    .msisdn(customer.get().getMsisdn())
                    .mime(MIME.PDF)
                    .build());
            if(statement!=null)
                prompt = "Done"+Emoji.Smiley+Emoji.ThumbsUp+"Here you go, please click the link below to download"+Emoji.PointDown+"\n"+statement.getFileDownloadUri().replace("http://217.15.118.15",statementServiceConfigurationProperties.getNgrokServiceEndpointUrl())+"\n\nIs there anything else I can assist you with?"+Emoji.Smiley;
            else prompt = "Oops!!"+Emoji.Pensive+"Something went wrong with the transaction\nWould you like to try this again?";
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+Emoji.Smiley+", but before we get your statement, what is your Ecocash number?";
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
            context = createTicket(webhookRequest, UseCase.SUBSCRIBER_STATEMENT);
            Statement statement = customerService.getStatement(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()), StatementRequest.builder()
                    .endDate(map.get("startdate").toString())
                    .startDate(map.get("enddate").toString())
                    .msisdn(customer.get().getMsisdn())
                    .mime(MIME.PDF)
                    .build());
            if(statement!=null)
                prompt = "Done"+Emoji.Smiley+Emoji.ThumbsUp+"Here you go, please click the link below to download"+Emoji.PointDown+"\n"+statement.getFileDownloadUri().replace("http://217.15.118.15",statementServiceConfigurationProperties.getNgrokServiceEndpointUrl())+"\n\nIs there anything else I can assist you with?"+Emoji.Smiley;
            else prompt = "Oops!!"+Emoji.Pensive+"Something went wrong with the transaction\nWould you like to try this again?";
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+Emoji.Smiley+", but before we get your statement, what is your Ecocash number?";
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
        return getWebhookResponse(webhookRequest,prompt,createTicket(webhookRequest, UseCase.SUBSCRIBER_STATEMENT), UseCase.SUBSCRIBER_STATEMENT);
    }

    private WebhookResponse payBillerUsecaseGetBillerConfirmationNegativeHandler(WebhookRequest webhookRequest) {
        String prompt = "That's ok"+ Emoji.Weary+" . Would you want to re-try?";
        return getWebhookResponse(webhookRequest, prompt, null, UseCase.BILL_PAYMENT);
    }

    private WebhookResponse payBillerUsecaseGetBillerConfirmationNegativeAffirmHandler(WebhookRequest webhookRequest) {
        String prompt = "Wonderful "+Emoji.Smiley+" is there anything else I can do for you?";
        return getWebhookResponse(webhookRequest, prompt, null, UseCase.BILL_PAYMENT);
    }

    private WebhookResponse payBillerUsecaseGetBillerConfirmationNegativeNegHandler(WebhookRequest webhookRequest) {
        String prompt = "Ok "+Emoji.Grimacing+" lets do this one more time. Enter the merchant code "+Emoji.PointDown;
        return getWebhookResponse(webhookRequest, prompt, closeTicket(webhookRequest, TicketStatus.CLOSED), UseCase.BILL_PAYMENT);
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
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+Emoji.Smiley+", but before we pay your bill, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse payBillerUsecaseGetBillerConfirmationFallbackHandler(WebhookRequest webhookRequest) {
        String prompt = "Does this mean you confirm the details of the transaction above?"+Emoji.PointUp;
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.BILL_PAYMENT);
    }

    private WebhookResponse payBillerUsecaseGetBillerAccountHandler(WebhookRequest webhookRequest) {
        String prompt = "Alrighty"+Emoji.Smiley+" How much do you want to pay?";
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.BILL_PAYMENT);
    }

    private WebhookResponse payBillerUsecaseGetBillerAccountFallbackHandler(WebhookRequest webhookRequest) {
        String prompt = "Sorry that won't work"+Emoji.Pensive+" type the biller account as a number e.g 1234";
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.BILL_PAYMENT);
    }

    private WebhookResponse payBillerUsecaseGetBillerCodeHandler(WebhookRequest webhookRequest) {
        String  prompt = "Got it"+Emoji.Smiley+" What is your Account Number?";
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.BILL_PAYMENT);
    }

    private WebhookResponse payBillerUsecaseGetBillerCodeFallbackHandler(WebhookRequest webhookRequest) {
        String prompt = "I can't work with that"+Emoji.Pensive+"enter the biller code as a number e.g., 181818";
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.BILL_PAYMENT);
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
        Object[] contexts=null;
        if(customer.isPresent()) {
            if(transactionResponse.getField1().equalsIgnoreCase("200"))
            prompt = "Alright thats fine,"+Emoji.Smiley+"\nSo in summary you want to pay $ZWL"+ map.get("amount").toString() +" to biller code "+map.get("biller.original").toString()+" ("+transactionResponse.getField6()+") for account "+map.get("number.original").toString()+"\ncan you confirm this is correct?";
            else{
                prompt = "Ooops,"+Emoji.Pensive+" It seems the Biller you entered "+map.get("biller.original").toString()+" Is not recognised. Please re-check and enter the valid biller code";
                OutputContext outputContext = OutputContext.builder()
                        .lifespanCount(1)
                        .name(webhookRequest.getSession()+"/contexts/awaiting_biller_code")
                        .build();
                OutputContext outputContext1 = OutputContext.builder()
                        .lifespanCount(0)
                        .name(webhookRequest.getSession()+"/contexts/awaiting_pay_bill_confirmation")
                        .build();
                contexts = new Object[]{outputContext, outputContext1};
            }
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+Emoji.Smiley+", but before we pay your bill, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .outputContexts(contexts)
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;

    }

    private WebhookResponse payBillerUsecaseGetBillerAmountFallbackHandler(WebhookRequest webhookRequest) {
        String prompt = "Oops, that wo'nt work"+Emoji.Pensive+"Type the amount you want to pay e.g., 20 dollars/ $20";
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.BILL_PAYMENT);
    }

    private WebhookResponse payBillerUsecaseScenario2Handler(WebhookRequest webhookRequest) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        BillerLookupRequest billerLookupRequest = new BillerLookupRequest();
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> map = objectMapper.convertValue(outputContexts.get(0).getParameters(),Map.class);
        Map<String,Object> payment = objectMapper.convertValue(map.get("payment"),Map.class);
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        billerLookupRequest.setBiller(map.get("biller.original").toString());
        TransactionResponse transactionResponse = paymentGatewayProcessor.lookupBiller(billerLookupRequest);
        String prompt;
        Object[] contexts = null;
        if(customer.isPresent()) {
            if(transactionResponse.getField1().equalsIgnoreCase("200")) {
                prompt = "Alright thats fine," + Emoji.Smiley + "\nSo in summary you want to pay $ZWL"
                        + payment.get("amount").toString() + " to biller code "
                        + map.get("biller.original").toString() + " ("
                        + transactionResponse.getField6() + ") for account "
                        + map.get("number.original").toString() + "\ncan you confirm this is correct?";
                contexts = createTicket(webhookRequest, UseCase.BILL_PAYMENT);
            }
            else{
                prompt = "Ooops,"+Emoji.Pensive+" It seems the Biller you entered "+map.get("biller.original").toString()+" Is not recognised. Please re-check and enter the valid biller code";
                OutputContext outputContext = OutputContext.builder()
                        .lifespanCount(1)
                        .name(webhookRequest.getSession()+"/contexts/awaiting_biller_code")
                        .build();
                OutputContext outputContext1 = OutputContext.builder()
                        .lifespanCount(0)
                        .name(webhookRequest.getSession()+"/contexts/awaiting_pay_bill_confirmation")
                        .build();
                Ticket ticket = new Ticket();
                ticket.setProfile(profileRepository.getByChatId(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest())).orElseThrow(() -> new ProfileNotFoundException(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()))));
                ticket.setTicketStatus(TicketStatus.OPEN);
                if (webhookRequest.getQueryResult().getSentimentAnalysisResult() != null)
                    ticket.setSentimentStart(webhookRequest.getQueryResult().getSentimentAnalysisResult().getQueryTextSentiment().getScore());
                else ticket.setSentimentStart(0.0);
                ticket.setOriginalQueryText(webhookRequest.getQueryResult().getQueryText());
                ticket.setUsecase(UseCase.BILL_PAYMENT);
                ticketRepository.save(ticket);
                TicketParameter ticketParameter = TicketParameter.builder()
                        .id(ticket.getId())
                        .build();
                OutputContext outputContext2 = OutputContext.builder()
                        .lifespanCount(50)
                        .name(webhookRequest.getSession() + "/contexts/ticket")
                        .parameters(ticketParameter)
                        .build();

                contexts = new Object[]{outputContext, outputContext1, outputContext2};
            }
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+Emoji.Smiley+", but before we pay your bill, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .outputContexts(contexts)
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse payBillerUsecaseScenario1Handler(WebhookRequest webhookRequest) {
        String prompt = "I can help you do that"+Emoji.Smiley+"\nWhat is the biller code or Name of the organisation you want to pay?";
        return getWebhookResponse(webhookRequest,prompt,createTicket(webhookRequest, UseCase.BILL_PAYMENT), UseCase.BILL_PAYMENT);
    }

    private WebhookResponse payBillerUsecaseMoreAffirmativeHandler(WebhookRequest webhookRequest) {
        String prompt = "Ok great. What else can I help you do?"+Emoji.Smiley;
        return getWebhookResponse(webhookRequest,prompt,closeTicket(webhookRequest, TicketStatus.CLOSED), UseCase.BILL_PAYMENT);
    }

    private WebhookResponse payBillerUsecaseMoreNegativeHandler(WebhookRequest webhookRequest) {
        String prompt = "Ok great. Have a nice day and remember to continue living life the Ecocash way"+Emoji.Smiley;
        return getWebhookResponse(webhookRequest,prompt,closeTicket(webhookRequest, TicketStatus.CLOSED), UseCase.BILL_PAYMENT);
    }

    private WebhookResponse payBillerUsecaseMoreFallbackHandler(WebhookRequest webhookRequest) {
        String prompt = "Do you mean you still need me to help you?."+Emoji.Smiley;
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.BILL_PAYMENT);
    }

    /*
    *PIN Reset Usecase
    *
     */
    private WebhookResponse pinresetUsecaseSecurityQuestionsFirstAnswerHandler(WebhookRequest webhookRequest) {
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
        String customerAnswer = webhookRequest.getQueryResult().getQueryText();
        //customerAnswer = passwordEncoder.encode(customerAnswer);
        String prompt=null;
        OutputContext outputContext=null;
        OutputContext outputContext1=null;
        if(customer.isPresent()) {
            List<Answer> answers = selfServiceCoreProcessor.getAnswerByMsisdnAndAnswerStatus(customer.get().getMsisdn());
            Double i = (Double) map.get("question");
            Answer answer = answers.get(i.intValue()-1);
//            if(passwordEncoder.matches(customerAnswer,answer.getAnswer())){
            if(true){
                if(i.intValue()<answers.size()) {
                    Answer newAnswer = answers.get(i.intValue());
                    prompt = newAnswer.getQuestion().getText();
                    TicketParameter ticketParameter = TicketParameter.builder()
                            .id(Double.valueOf(map.get("id").toString()).longValue())
                            .question(i.intValue()+1)
                            .build();
                    outputContext = OutputContext.builder()
                            .lifespanCount(50)
                            .name(webhookRequest.getSession() + "/contexts/ticket")
                            .parameters(ticketParameter)
                            .build();
                    outputContext1 = OutputContext.builder()
                            .lifespanCount(1)
                            .name(webhookRequest.getSession() + "/contexts/awaiting_answers")
                            .build();
                }else{
                    if(customerService.pinReset(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()))) {
                        prompt = "well done" + Emoji.ThumbsUp + "I have reset your PIN. You will receive an SMS on your phone with your new temporary PIN. You will be prompted to change this when you dial *151#. \nAnything else I can do for you" + Emoji.Smiley;
                        outputContext = OutputContext.builder()
                                .lifespanCount(50)
                                .name(webhookRequest.getSession() + "/contexts/ticket")
                                .build();
                        outputContext1 = OutputContext.builder()
                                .lifespanCount(1)
                                .name(webhookRequest.getSession() + "/contexts/awaiting_pinreset_more")
                                .build();
                        return WebhookResponse.builder()
                                .fulfillmentText(prompt)
                                .outputContexts(new Object[]{outputContext,outputContext1})
                                .source("ecocashchatbotcore")
                                .build();
                    }else {
                        prompt = "Oops" + Emoji.Pensive + "An error occured processing your request please try again later";
                        return WebhookResponse.builder()
                                .fulfillmentText(prompt)
                                .source("ecocashchatbotcore")
                                .build();
                    }
                }
            } else {
                prompt = "Your answer is wrong. An agent will be in touch shortly to verify your identity";
                return WebhookResponse.builder()
                        .fulfillmentText(prompt)
                        .source("ecocashchatbotcore")
                        .outputContexts(closeTicket(webhookRequest, TicketStatus.ESCALATED))
                        .build();
            }
        }
        else {
            prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(), customer) + Emoji.Smiley + ", but before we reset your PIN, what is your Ecocash number?";
            OutputContext goForVerification = OutputContext.builder()
                    .lifespanCount(5)
                    .name(webhookRequest.getSession()+"/contexts/awaiting_verify_msisdn")
                    .build();
            return WebhookResponse.builder()
                    .fulfillmentText(prompt)
                    .outputContexts(new Object[]{goForVerification})
                    .source("ecocashchatbotcore")
                    .build();
        }
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .outputContexts(new Object[]{outputContext,outputContext1})
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse pinresetUsecaseSecurityQuestionsAffirmativeHandler(WebhookRequest webhookRequest) {
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
        OutputContext outputContext=null;
        String prompt;
        if(customer.isPresent()) {
            List<Answer> answers = selfServiceCoreProcessor.getAnswerByMsisdnAndAnswerStatus(customer.get().getMsisdn());
            if(map.containsKey("question")){
                Double i = (Double) map.get("question");
                if(i.intValue()<answers.size()-1) {
                    Answer answer = answers.get(i.intValue());
                    prompt = answer.getQuestion().getText();
                    TicketParameter ticketParameter = TicketParameter.builder()
                            .id(Double.valueOf(map.get("id").toString()).longValue())
                            .question(i.intValue()+1)
                            .build();
                    outputContext = OutputContext.builder()
                            .lifespanCount(50)
                            .name(webhookRequest.getSession() + "/contexts/ticket")
                            .parameters(ticketParameter)
                            .build();
                }else{
                    prompt="well done";
                }
            }else{
                prompt = answers.get(0).getQuestion().getText();
                TicketParameter ticketParameter = TicketParameter.builder()
                        .id(Double.valueOf(map.get("id").toString()).longValue())
                        .question(1)
                        .build();
                outputContext = OutputContext.builder()
                        .lifespanCount(50)
                        .name(webhookRequest.getSession() + "/contexts/ticket")
                        .parameters(ticketParameter)
                        .build();
            }
        }
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+Emoji.Smiley+", but before we reset your PIN, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt.toString())
                .source("ecocashchatbotcore")
                .outputContexts(new Object[]{outputContext} )
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
        else prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer)+Emoji.Smiley+", but before we reset your PIN, what is your Ecocash number?";
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .outputContexts(createTicket(webhookRequest, UseCase.PIN_RESET))
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private WebhookResponse pinresetUsecaseSecurityQuestionsMoreAffirmativeHandler(WebhookRequest webhookRequest) {
        String prompt = "Ok great. What else can I help you do?"+Emoji.Smiley;
        return getWebhookResponse(webhookRequest,prompt,closeTicket(webhookRequest, TicketStatus.CLOSED), UseCase.PIN_RESET);
    }

    private WebhookResponse pinresetUsecaseSecurityQuestionsMoreNegativeHandler(WebhookRequest webhookRequest) {
        String prompt = "Ok great. Have a nice day and remember to continue living life the Ecocash way"+Emoji.Smiley;
        return getWebhookResponse(webhookRequest,prompt,closeTicket(webhookRequest, TicketStatus.CLOSED), UseCase.PIN_RESET);
    }


    private WebhookResponse pinresetUsecaseSecurityQuestionsFallbackHandler(WebhookRequest webhookRequest) {
        String prompt = "Do you mean you want to answer your security questions?."+Emoji.Confused;
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.PIN_RESET);
    }


    private WebhookResponse pinresetUsecaseSecurityQuestionsMoreFallbackHandler(WebhookRequest webhookRequest) {
        String prompt = "Do you mean you still need me to help you?."+Emoji.Smiley;
        return getWebhookResponse(webhookRequest,prompt,null, UseCase.PIN_RESET);
    }

    private WebhookResponse welcomeHandler(WebhookRequest webhookRequest){
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        String prompt;
        if(customer.isPresent())
            prompt = promptProcessor(promptService.findByIntentAndStage("Default Welcome Intent",1).getText(),webhookRequest,customer);
        else prompt = promptProcessor(promptService.findByIntentAndStage("Default Welcome Intent",2).getText(),webhookRequest,customer);
        WebhookResponse webhookResponse = WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .build();
        log.info("Sending response to dialogFlow: {}\n", webhookResponse);
        return webhookResponse;
    }

    private Optional<Customer> isNewCustomer(WebhookRequest webhookRequest){
        Optional<Customer> customer;
        Optional<Profile> profile = profileRepository.getByChatId(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()));
        if(profile.isPresent()&&profile.get().isVerified()) {
            customer = customerRepository.findByProfilesChatId(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()));
        }else customer = customerRepository.findByProfilesChatId("not verified");
        return customer;
    }

    private WebhookResponse getWebhookResponse(WebhookRequest webhookRequest, String prompt, Object[] contexts, UseCase usecase) {
        log.info("Processing dialogflow intent: {}", webhookRequest.getQueryResult().getIntent().getDisplayName());
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        if(!customer.isPresent()) {
            prompt = "Alright " + DialogFlowUtil.getAlias(webhookRequest.getOriginalDetectIntentRequest(),customer) + Emoji.Smiley + ", but before we do that, what is your Ecocash number?";
            OutputContext goForVerification = OutputContext.builder()
                    .lifespanCount(5)
                    .name(webhookRequest.getSession()+"/contexts/awaiting_verify_msisdn")
                    .parameters(UnverifiedCustomerParameter.builder().usecase(usecase).build())
                    .build();
            contexts = new Object[]{goForVerification};
        }
        return WebhookResponse.builder()
                .fulfillmentText(prompt)
                .source("ecocashchatbotcore")
                .outputContexts(contexts)
                .build();
    }

    private Object[] closeTicket(WebhookRequest webhookRequest, TicketStatus ticketStatus){
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        ObjectMapper objectMapper = new ObjectMapper();
        int ticketIndex=0;
        for(int i = 0;i<outputContexts.size();i++){
            Map<String,Object> map = objectMapper.convertValue(outputContexts.get(i),Map.class);
            if(map.get("name").toString().equalsIgnoreCase(webhookRequest.getSession()+"/contexts/ticket")) ticketIndex=i;
        }
        OutputContext outputContext=null;
        Map<String,Object> map = objectMapper.convertValue(outputContexts.get(ticketIndex).getParameters(),Map.class);
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        if(customer.isPresent()) {
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

    private Object[] createTicket(WebhookRequest webhookRequest, UseCase usecase) {
        Optional<Customer> customer = isNewCustomer(webhookRequest);
        if(customer.isPresent())
        {
            Ticket ticket = new Ticket();
            ticket.setProfile(profileRepository.getByChatId(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest())).orElseThrow(() -> new ProfileNotFoundException(DialogFlowUtil.getChatId(webhookRequest.getOriginalDetectIntentRequest()))));
            ticket.setTicketStatus(TicketStatus.OPEN);
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

    private WebhookResponse resumeConversation(WebhookRequest webhookRequest) {
        List<OutputContext> outputContexts = webhookRequest.getQueryResult().getOutputContexts();
        ObjectMapper objectMapper = new ObjectMapper();
        int usecaseIndex=0;
        for(int i = 0;i<outputContexts.size();i++){
            Map<String,Object> map = objectMapper.convertValue(outputContexts.get(i),Map.class);
            if(map.get("name").toString().equalsIgnoreCase(webhookRequest.getSession()+"/contexts/awaiting_verify_msisdn")) usecaseIndex=i;
        }
        Map<String,Object> map = objectMapper.convertValue(outputContexts.get(usecaseIndex).getParameters(),Map.class);
        String prompt = "Great! you have been verified" + Emoji.ThumbsUp + Emoji.Grin + "Now that we have that out of the way... ";
        WebhookResponse webhookResponse;
        switch (UseCase.valueOf(map.get("usecase").toString()) ){
            case PIN_RESET:
                break;
            case BUY_AIRTIME:
                break;
            case SEND_MONEY:
                break;
            case MERCHANT_PAYMENT:
                break;
            case BILL_PAYMENT:
                break;
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
        }

        return null;
    }

    private String promptProcessor(String unprocessed, WebhookRequest webhookRequest, Optional<Customer> customer){
        String[] phrase = unprocessed.split("\\s");
        String processed="";
        for(String word:phrase){
            FunctionAdapter fa = Function.lookup(word);
            if(fa!=null) {
                if(!fa.hasArgs()) processed +=" "+fa.process();
                else {
                    PromptObject args = PromptObject.builder().webhookRequest(webhookRequest).customer(customer.get()).build();
                    processed +=" "+fa.process(args);
                }
            }
            else processed+=" "+word;
        }
        return processed;
    }


}