package zw.co.cassavasmartech.ecocashchatbotcore.payment;

import com.econetwireless.common.strategies.service.ServiceLookup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionType;

@Component
public class PaymentRequestProcessorUtil {

    @Autowired
    private ServiceLookup serviceLookup;

    public PaymentRequestProcessor getPaymentRequestProcessor(TransactionType transactionType) {
        return serviceLookup.lookup((Class<? extends PaymentRequestProcessor>) transactionType.getRequestHandlerClass(), true);
    }
}
