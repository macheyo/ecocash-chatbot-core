package zw.co.cassavasmartech.ecocashchatbotcore.process.adapters;

import com.econetwireless.common.strategies.service.ServiceLookup;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.esb.model.Transaction;
import zw.co.cassavasmartech.esb.notification.NotificationProcessor;
import zw.co.cassavasmartech.esb.process.ProcessConstants;
import zw.co.cassavasmartech.esb.transaction.TransactionService;

import java.util.Optional;

@Slf4j
@Component
public class NotificationAdapter implements JavaDelegate {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ServiceLookup serviceLookup;


    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        try {
            String paymentReference = (String) delegateExecution.getVariable(ProcessConstants.PAYMENT_REFERENCE);
            Optional<Transaction> transactionLookupResult = transactionService.findByPaymentReference(String.valueOf(paymentReference));
            transactionLookupResult.ifPresent(transaction -> {
                NotificationProcessor notificationProcessor = serviceLookup.lookup((Class<? extends NotificationProcessor>) transaction.getProcessRegister().getNotificationHandlerClass(), true);
                notificationProcessor.sendNotification(transaction);
            });
        } catch (Exception e) {
            log.error("Exception during notification : {}", e);
        }
    }

}