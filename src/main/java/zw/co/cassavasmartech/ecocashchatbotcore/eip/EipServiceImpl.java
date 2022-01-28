package zw.co.cassavasmartech.ecocashchatbotcore.eip;

import com.twilio.rest.api.v2010.account.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.common.Util;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.*;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.BusinessException;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.MerchantNotFoundException;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.TicketNotFoundException;
import zw.co.cassavasmartech.ecocashchatbotcore.infobip.InfobipService;
import zw.co.cassavasmartech.ecocashchatbotcore.infobip.data.*;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Profile;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Ticket;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.TicketRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.service.TicketService;
import zw.co.cassavasmartech.ecocashchatbotcore.telegram.TelegramService;
import zw.co.cassavasmartech.ecocashchatbotcore.twilio.TwilioService;

@Slf4j
@Component
public class EipServiceImpl implements EipService {
    @Autowired
    EipInvoker eipInvoker;
    @Autowired
    MerchantRepository merchantRepository;
    @Autowired
    TicketService ticketService;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    InfobipService infobipService;
    @Autowired
    TwilioService twilioService;
    @Autowired
    TelegramService telegramService;
    @Autowired
    EipConfigurationProperties eipConfigurationProperties;


    @Override
    public EipTransaction postPayment(SubscriberToMerchantRequest subscriberToMerchantRequest) {
        EipTransaction eipTransaction = getSubscriberToMerchantTransaction(subscriberToMerchantRequest);
        EipTransaction eipTransaction1 = eipInvoker.invoke(eipTransaction).orElseThrow(() -> new BusinessException("EIP returned a null response"));
        EipTransaction eipTransaction2 = eipTransaction1;
        return eipTransaction1;
    }

    @Override
    public Boolean handleCallback(EipTransaction response) {
        log.info("Processing callback request: {}", response);
        Profile profile = ticketService.findProfileByReference(response.getReferenceCode());
        Ticket ticket = ticketService.findByReference(response.getReferenceCode());
        Boolean isNotified = false;
        String notification;
        switch (response.getTransactionOperationStatus()) {
                case "COMPLETED":
                    log.info(" transaction completed");
                    notification = "Great! transaction of "
                            +response.getPaymentAmount().getCharginginformation().getCurrency()
                            +response.getPaymentAmount().getCharginginformation().getAmount()
                            +" to "
                            +response.getMerchantCode()
                            +" ("+response.getPaymentAmount().getChargeMetaData().getOnBeHalfOf()+") "
                            +"has been successful. Is there anything else you would like me to do for you?";
                    isNotified =sendNotificationToPlatform(ticket.getConversationId(),profile,notification);
                    break;
                case "PENDING SUBSCRIBER VALIDATION":
                    log.info("transaction not completed:{} because its still pending ");
                    notification = "Transaction of "
                            +response.getPaymentAmount().getCharginginformation().getCurrency()
                            +response.getPaymentAmount().getCharginginformation().getAmount()
                            +" to "
                            +response.getMerchantCode()
                            +" ("+response.getPaymentAmount().getChargeMetaData().getOnBeHalfOf()+") "
                            +"is still pending your approval, we are waiting for you to input your PIN on the USSD push on your phone. Shall we try the transaction again?";
                    isNotified = sendNotificationToPlatform(ticket.getConversationId(), profile,notification);
                case "FAILED":
                    notification = "Transaction of "
                            +response.getPaymentAmount().getCharginginformation().getCurrency()
                            +response.getPaymentAmount().getCharginginformation().getAmount()
                            +" to "
                            +response.getMerchantCode()
                            +" ("+response.getPaymentAmount().getChargeMetaData().getOnBeHalfOf()+") "
                            +"has failed. Shall we try the transaction again?";
                    isNotified = sendNotificationToPlatform(ticket.getConversationId(), profile,notification);
                    log.info(" transaction Failed");
                    break;
            }
            return isNotified;
    }

    private Boolean sendNotificationToPlatform(String conversationId, Profile profile, String message){
        Content content = new Content();
        content.setText(message);
        switch (profile.getPlatform()){
            case WHATSAPP:
                ApiResponse<InfoBipRequest> response = infobipService.sendMessage(OutboundRequest.builder()
                        .channel(Channel.WHATSAPP)
                        .conversationId(conversationId)
                        .to(profile.getChatId())
                        .contentType(ContentType.TEXT)
                        .content(content)
                        .build());
                log.info("Infobip Adapter response {}",response);
                if(response.getStatus() == HttpStatus.OK.value()) return true;
                return false;
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

    private EipTransaction getSubscriberToMerchantTransaction(SubscriberToMerchantRequest subscriberToMerchantRequest) {
//        Merchant merchant = merchantRepository.findByMerchantCode(subscriberToMerchantRequest.getMerchantCode()).orElseThrow(()->new MerchantNotFoundException(subscriberToMerchantRequest.getMerchantCode()));
        String reference = Util.generateReference(subscriberToMerchantRequest.getMsisdn());
        Ticket ticket = ticketRepository.findById(subscriberToMerchantRequest.getTicketId()).orElseThrow(()->new TicketNotFoundException(subscriberToMerchantRequest.getTicketId().toString()));
        ticket.setReference(reference);
        ticketRepository.save(ticket);
        return EipTransaction.builder()
                .clientCorrelator(reference)
                .notifyUrl(eipConfigurationProperties.getNotifyUrl())
                .referenceCode(reference)
                .tranType("MER")
                .endUserId(subscriberToMerchantRequest.getMsisdn())
                .remarks(subscriberToMerchantRequest.getMerchantName())
                .transactionOperationStatus("Charged")
                .merchantCode(subscriberToMerchantRequest.getMerchantCode())
                .merchantPin("4827")
                .merchantNumber(subscriberToMerchantRequest.getMerchantMsisdn())
                .currencyCode(eipConfigurationProperties.getCurrency())
                .countryCode("ZW")
                .terminalID("web")
                .location(subscriberToMerchantRequest.getMerchantName())
                .superMerchantName(subscriberToMerchantRequest.getMerchantName())
                .merchantName(subscriberToMerchantRequest.getMerchantName())
                .paymentAmount(
                    PaymentAmount.builder()
                            .chargeMetaData(
                                    ChargeMetaData.builder()
                                            .onBeHalfOf(subscriberToMerchantRequest.getMerchantName())
                                            .channel("WEB")
                                            .purchaseCategoryCode("Online Payment")
                                            .build()
                            )
                            .charginginformation(
                                    Charginginformation.builder()
                                            .currency(eipConfigurationProperties.getCurrency())
                                            .amount(subscriberToMerchantRequest.getAmount())
                                            .description("Escrow Online Payment")
                                            .build())
                            .build()
                )
                .build();
    }

}
