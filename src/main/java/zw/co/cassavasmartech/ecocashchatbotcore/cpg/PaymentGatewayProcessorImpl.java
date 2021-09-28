package zw.co.cassavasmartech.ecocashchatbotcore.cpg;

import com.twilio.rest.api.v2010.account.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.ecocashchatbotcore.common.Util;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.data.*;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.BusinessException;
import zw.co.cassavasmartech.ecocashchatbotcore.model.*;
import zw.co.cassavasmartech.ecocashchatbotcore.service.TicketService;
import zw.co.cassavasmartech.ecocashchatbotcore.telegram.TelegramService;
import zw.co.cassavasmartech.ecocashchatbotcore.twilio.TwilioService;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentGatewayProcessorImpl implements PaymentGatewayProcessor {

    @Autowired
    TicketService ticketService;
    @Autowired
    TelegramService telegramService;
    @Autowired
    TwilioService twilioService;
    private final CheckSumGenerator checksumGenerator;
    private final PaymentGatewayInvoker paymentGatewayInvoker;
    private final CpgConfigurationProperties cpgConfigProperties;
    @Value("${ecocash.chatbot.core.cpg-api.vendorGIGAIOTCode}")
    private String vendorGIGAIOTCode;
    @Value("${ecocash.chatbot.core.cpg-api.vendorGIGAIOTApiKey}")
    private String vendorGIGAIOTApiKey;
    @Value("${ecocash.chatbot.core.cpg-api.vendorEPGCode}")
    private String vendorEPGCode;
    @Value("${ecocash.chatbot.core.cpg-api.vendorEPGApiKey}")
    private String vendorEPGApiKey;
    @Value("${ecocash.chatbot.core.cpg-api.vendorSASAICode}")
    private String vendorSASAICode;
    @Value("${ecocash.chatbot.core.cpg-api.vendorSASAIApiKey}")
    private String vendorSASAIApiKey;

    @Override
    public TransactionResponse merchantToSubscriber(MerchantToSubscriberRequest merchantToSubscriberRequest) {
        return invokeApi(getMerchantTosubscriberRequest(merchantToSubscriberRequest));
    }

    @Override
    public TransactionResponse subscriberToMerchant(SubscriberToMerchantRequest subscriberToMerchantRequest) {
        final TransactionRequest transactionRequest = getTransactionRequest(subscriberToMerchantRequest);
        log.debug("Processing subscriber to merchant request");
        return invokeApi(transactionRequest);

    }

    @Override
    public TransactionResponse merchantToMerchant(MerchantToMerchantRequest merchantToMerchantRequest) {
        final TransactionRequest transactionRequest = getMerchantToMerchantRequest(merchantToMerchantRequest);
        log.debug("Processing subscriber to merchant request");
        return invokeApi(transactionRequest);
    }

    @Override
    public TransactionResponse pinReset(String msisdn) {
        final TransactionRequest transactionRequest = getPinResetRequest(msisdn);
        log.debug("Processing Pin Reset request");
        return invokeApi(transactionRequest);
    }

    @Override
    public TransactionResponse lookupCustomer(String msisdn) {
        final TransactionRequest transactionRequest = getLookUpCustomerRequest(msisdn);
        log.debug("Processing Lookup Customer request");
        return invokeApi2(transactionRequest);
    }

    @Override
    public TransactionResponse subscriberToSubscriber(SubscriberToSubscriberRequest subscriberToSubscriberRequest) {
        final TransactionRequest transactionRequest = getSubscriberToSubscriberRequest(subscriberToSubscriberRequest);
        log.debug("Processing send money Peer to Peer");
        return invokeApi(transactionRequest);
    }

    @Override
    public TransactionResponse subscriberToBiller(SubscriberToBillerRequest subscriberToBillerRequest) {
        final TransactionRequest transactionRequest = getSubscriberToBillerRequest(subscriberToBillerRequest);
        log.debug("Processing send money subscriber to Biller");
        return invokeApi(transactionRequest);
    }

    @Override
    public TransactionResponse lookupBiller(BillerLookupRequest billerLookupRequest) {
        final TransactionRequest transactionRequest = getBillerLookupRequest(billerLookupRequest);
        log.debug("Processing biller lookup");
        return invokeApi(transactionRequest);
    }

    @Override
    public TransactionResponse subscriberAirtime(SubscriberAirtimeRequest subscriberAirtimeRequest) {
        final TransactionRequest transactionRequest = getSubscriberAirtimeRequest(subscriberAirtimeRequest);
        log.debug("Processing airtime request");
        return invokeApi(transactionRequest);
    }

    @Override
    public TransactionResponse registerCustomer(Registration registration) {
        final TransactionRequest transactionRequest = getRegistrationRequest(registration);
        log.debug("Processing registration request");
        return invokeApi(transactionRequest);
    }

    @Override
    public Boolean handleCallBack(PostTransaction postTransaction) {
        Profile profile = ticketService.findByReference(postTransaction.getTransactionRequest().getField3());
        Boolean isNotified = false;
        String notification;
        if(postTransaction.getTransactionRequest().getField1().equalsIgnoreCase("200"))
        notification = "Done, your transaction was successfull. Is there anything else you would like me to do for you?";
        else notification = "Ooops!!! Your transaction failed. Is there anything else you would like me to do for you?";
        isNotified =sendNotificationToPlatform(profile,notification);
        return isNotified;
    }


    @Override
    public TransactionResponse getStatement(String msisdn) {
        final TransactionRequest transactionRequest = getStatementRequest(msisdn);
        log.debug("Processing get Statement request");
        return invokeApi(transactionRequest);
    }


    private TransactionResponse invokeApi(TransactionRequest transactionRequest) {
        final PostTransaction postTransaction = new PostTransaction();
        postTransaction.setTransactionRequest(transactionRequest);
        return paymentGatewayInvoker.invoke(postTransaction)
                .map(PostTransactionResponse::getTransactionResponse)
                .orElseThrow(() -> new BusinessException("Received null response from payment gateway"));
    }

    private TransactionResponse invokeApi2(TransactionRequest transactionRequest) {
        final PostTransaction postTransaction = new PostTransaction();
        postTransaction.setTransactionRequest(transactionRequest);
        return paymentGatewayInvoker.invoke2(postTransaction)
                .map(PostTransactionResponse::getTransactionResponse)
                .orElseThrow(() -> new BusinessException("Received null response from payment gateway"));
    }


    private TransactionRequest getLookUpCustomerRequest(String msisdn) {
        return RequestBuilder.newInstance()
                .vendorCode(vendorGIGAIOTCode)
                .checksumGenerator(checksumGenerator)
                .vendorApiKey(vendorGIGAIOTApiKey)
                .checksum("checksum")
                .tranType(cpgConfigProperties.getCustomerLookupTranType())
                .applicationCode("ecocashzw")
                .reference(Util.generateReference(msisdn))
                .msisdn(msisdn)
                .currency("ZWL")
                .build();
    }

    private TransactionRequest getBillerLookupRequest(BillerLookupRequest billerLookupRequest) {
        return RequestBuilder.newInstance()
                .vendorCode(vendorSASAICode)
                .vendorApiKey(vendorSASAIApiKey)
                .checksumGenerator(checksumGenerator)
                .tranType(cpgConfigProperties.getBillerLookupTranType())
                .msisdn(billerLookupRequest.getBiller())
                .applicationCode("ecocashzw")
                .reference(Util.generateReference(billerLookupRequest.getBiller()))
                .build();
    }

    private TransactionRequest getSubscriberAirtimeRequest(SubscriberAirtimeRequest subscriberAirtimeRequest){
        String reference = Util.generateReference(subscriberAirtimeRequest.getMsisdn1());
        Ticket ticket = ticketService.findById(subscriberAirtimeRequest.getTicketId());
        ticket.setReference(reference);
        ticketService.updateSingle(ticket);
        return RequestBuilder.newInstance()
                .vendorCode(vendorEPGCode)
                .vendorApiKey(vendorEPGApiKey)
                .checksumGenerator(checksumGenerator)
                .msisdn(subscriberAirtimeRequest.getMsisdn1())
                .applicationCode("ecocashzw")
                .reference(reference)
                .callbackUrl(cpgConfigProperties.getCpgCallBackUrl())
                .msisdn2(subscriberAirtimeRequest.getMsisdn2())
                .amount(subscriberAirtimeRequest.getAmount())
                .tranType(cpgConfigProperties.getSubscriberAirtimeTranType())
                .build();
    }

    private TransactionRequest getStatementRequest(String msisdn) {
        return RequestBuilder.newInstance()
                .vendorCode("EPGTESTPT")
                .checksumGenerator(checksumGenerator)
                .tranType("00029")
                .applicationCode("ecocashzw")
                .reference(Util.generateReference(msisdn))
                .securityMode("101")
                .vendorApiKey("abf8988717c777874645af9e60db6e607dd5962c6e9c821f775c515618d2393e")
                .msisdn(msisdn)
                .build();
    }

    private TransactionRequest getTransactionRequest(SubscriberToMerchantRequest request) {
        return RequestBuilder.newInstance()
                .vendorCode(vendorEPGCode)
                .vendorApiKey(vendorEPGApiKey)
                .checksumGenerator(checksumGenerator)
                .currency("RTGS")
                .pin(request.getPin())
                .amount(String.valueOf(request.getAmount()))
                .tranType(cpgConfigProperties.getSubscriberToMerchantTranType())
                .msisdn2(request.getMerchantMsisdn())
                .msisdn(request.getSubscriberMsisdn())
                .reference(Util.generateReference(request.getSubscriberMsisdn()))
                .applicationCode("ecocashzw")
                .build();
    }

    private TransactionRequest getSubscriberToBillerRequest(SubscriberToBillerRequest request) {
        String reference = Util.generateReference(request.getMsisdn());
        Ticket ticket = ticketService.findById(request.getTicketId());
        ticket.setReference(reference);
        ticketService.updateSingle(ticket);
        return RequestBuilder.newInstance()
                .vendorCode(vendorSASAICode)
                .vendorApiKey(vendorSASAIApiKey)
                .checksumGenerator(checksumGenerator)
                .msisdn(request.getMsisdn())
                .accountNumber(request.getBillerCode())
                .msisdn2(request.getBillerCode())
                .tranType(cpgConfigProperties.getSubscriberToBillerTranType())
                .applicationCode("ecocashzw")
                .callbackUrl(cpgConfigProperties.getCpgCallBackUrl())
                .msisdn2(request.getMsisdn2())
                .reference(reference)
                .currency("ZWL")
                .countryCode("ZW")
                .amount(String.valueOf(request.getAmount()))
                .build();
    }

    private TransactionRequest getSubscriberToSubscriberRequest(SubscriberToSubscriberRequest request) {
        String reference = Util.generateReference(request.getMsisdn1());
        Ticket ticket = ticketService.findById(request.getTicketId());
        ticket.setReference(reference);
        ticketService.updateSingle(ticket);
        return RequestBuilder.newInstance()
                .vendorCode(vendorSASAICode)
                .vendorApiKey(vendorSASAIApiKey)
                .msisdn(request.getMsisdn1())
                .checksumGenerator(checksumGenerator)
                .tranType(cpgConfigProperties.getSubscriberToSubscriberTranType())
                .applicationCode("ecocashzw")
                .reference(reference)
                .callbackUrl(cpgConfigProperties.getCpgCallBackUrl())
                .msisdn2(request.getMsisdn2())
                .amount(String.valueOf(request.getAmount()))
                .currency("RTGS")
                .countryCode("ZW")
                .build();

    }



    private TransactionRequest getMerchantTosubscriberRequest(MerchantToSubscriberRequest request) {
        return RequestBuilder.newInstance()
                .vendorCode(vendorGIGAIOTCode)
                .vendorApiKey(vendorGIGAIOTApiKey)
                .checksumGenerator(checksumGenerator)
                .currency(request.getCurrency())
                .amount(String.valueOf(request.getAmount()))
                .tranType(cpgConfigProperties.getMerchantToSubscriberTranType())
                .msisdn(request.getMerchantMsisdn())
                .msisdn2(request.getSubscriberMsisdn())
                .pin(request.getMerchantPin())
                .reference(request.getSourceRef())
                .build();
    }

    private TransactionRequest getMerchantToMerchantRequest(MerchantToMerchantRequest request) {
        return RequestBuilder.newInstance()
                .vendorCode(vendorGIGAIOTCode)
                .vendorApiKey(vendorGIGAIOTApiKey)
                .checksumGenerator(checksumGenerator)
                .currency(request.getCurrency())
                .amount(String.valueOf(request.getAmount()))
                .tranType(cpgConfigProperties.getMerchantToSubscriberTranType())
                .msisdn(request.getSourceMerchantMsisdn())
                .msisdn2(request.getDestinationMsisdn())
                .pin(request.getSourceMerchantPin())
                .reference(request.getSourceRef())
                .build();
    }

    private TransactionRequest getPinResetRequest(String msisdn) {
        return RequestBuilder.newInstance()
                .vendorCode(vendorSASAICode)
                .vendorApiKey(vendorSASAIApiKey)
                .checksumGenerator(checksumGenerator)
                .tranType(cpgConfigProperties.getPinResetTranType())
                .msisdn(msisdn)
                .callbackUrl(cpgConfigProperties.getCpgCallBackUrl())
                .applicationCode("ecocashzw")
                .reference(Util.generateReference(msisdn))
                .build();
    }


    private TransactionRequest getRegistrationRequest(Registration registration) {
        return RequestBuilder.newInstance()
                .vendorCode(vendorEPGCode)
                .vendorApiKey(vendorEPGApiKey)
                .applicationCode("ecocashzw")
                .checksumGenerator(checksumGenerator)
                .msisdn(registration.getMsisdn())
                .pin(registration.getPin())
                .tranType(cpgConfigProperties.getRegistrationTrasType())
                .reference(Util.generateReference(registration.getMsisdn()))
                .msisdn2(registration.getMsisdn2())
                .securityMode("202105")
                .build();
    }

    private Boolean sendNotificationToPlatform(Profile profile, String message){
        switch (profile.getPlatform()){
            case WHATSAPP:
                Message twilioResponse =twilioService.sendMessage(profile.getChatId(),message);
                log.info("twilio response {}",twilioResponse);
                return twilioResponse.getStatus().equals("sent");
            case FACEBOOK:
                break;
            case TELEGRAM:
                telegramService.sendMessage(profile.getChatId(), message);
                return true;
            case TWITTER:
                break;
            case INSTAGRAM:
                break;
            case SITE:
                break;
        }
        return false;
    }
}
