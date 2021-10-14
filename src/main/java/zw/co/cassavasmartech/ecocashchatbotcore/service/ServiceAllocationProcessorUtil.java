package zw.co.cassavasmartech.ecocashchatbotcore.service;

import com.econetwireless.common.strategies.service.ServiceLookup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionType;

@Component
public class ServiceAllocationProcessorUtil {

    @Autowired
    private ServiceLookup serviceLookup;

    public ServiceAllocationProcessor getServiceAllocationProcessr(TransactionType transactionType) {
        return serviceLookup.lookup((Class<? extends ServiceAllocationProcessor>) transactionType.getRequestHandlerClass(), true);
    }

}
