package zw.co.cassavasmartech.ecocashchatbotcore.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.model.ServiceAllocationRecord;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Transaction;
import zw.co.cassavasmartech.ecocashchatbotcore.notification.NotificationProcessor;
import zw.co.cassavasmartech.ecocashchatbotcore.paymentresponse.PaymentRecordService;
import zw.co.cassavasmartech.ecocashchatbotcore.serviceresponse.ServiceResponseService;
import zw.co.cassavasmartech.ecocashchatbotcore.sms.service.SmsSender;

import java.time.format.DateTimeFormatter;

import java.util.Optional;

@Service
@Slf4j
public class ZesaNotificationProcessorImpl implements NotificationProcessor {
    @Autowired
    private SmsSender smsSender;
    @Autowired
    private ServiceResponseService serviceResponseService;

    @Autowired
    private PaymentRecordService paymentRecordService;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public void sendNotification(Transaction transaction) {
        final Optional<ServiceAllocationRecord> serviceAllocationRecordOptional = serviceResponseService.findByTransaction(transaction);
        if (serviceAllocationRecordOptional.isPresent()) {
            final ServiceAllocationRecord serviceAllocationRecord = serviceAllocationRecordOptional.get();
            sendSms(transaction, serviceAllocationRecord);
        }
    }

    private void sendSms(Transaction transaction, ServiceAllocationRecord serviceAllocationRecord) {
        log.info("Sending sms for transaction :{}", transaction.getPaymentReference());
        String message = serviceAllocationRecord.getResponseData();
        final String token = serviceAllocationRecord.getResponseMessage();
        final String newToken = token.replaceAll("....", "$0 ");
        String formattedMessage = message.replace(token, newToken);
        smsSender.buildAndSend(transaction.getPartner().getSmsHeaderText(), transaction.getMsisdn1(), formattedMessage);
    }
}
