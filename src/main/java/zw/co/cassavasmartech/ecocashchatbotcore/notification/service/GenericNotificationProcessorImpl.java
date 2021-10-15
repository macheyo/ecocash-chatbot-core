package zw.co.cassavasmartech.ecocashchatbotcore.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Transaction;
import zw.co.cassavasmartech.ecocashchatbotcore.notification.NotificationProcessor;
import zw.co.cassavasmartech.ecocashchatbotcore.serviceresponse.ServiceResponseService;
import zw.co.cassavasmartech.ecocashchatbotcore.sms.Sms;
import zw.co.cassavasmartech.ecocashchatbotcore.sms.service.SmsSender;


@Service
public class GenericNotificationProcessorImpl implements NotificationProcessor {

    @Autowired
    private SmsSender smsSender;

    @Autowired
    private ServiceResponseService serviceResponseService;

    @Override
    public void sendNotification(Transaction transaction) {
        serviceResponseService.findByTransaction(transaction).ifPresent(sa-> smsSender.buildAndSend(transaction.getMsisdn1(), sa.getResponseData()));
    }
}
