package zw.co.cassavasmartech.ecocashchatbotcore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.esb.model.ServiceAllocationRecord;
import zw.co.cassavasmartech.esb.model.Transaction;
import zw.co.cassavasmartech.esb.payment.PaymentResponse;

@Service
@Slf4j
public class ServiceAllocationImpl implements ServiceAllocation{

    @Autowired
    private ServiceAllocationProcessorUtil serviceAllocationProcessorUtil;

    @Override
    public PaymentResponse processServiceAllocationRequest(Transaction transaction) {
        final ServiceAllocationProcessor processor = serviceAllocationProcessorUtil.getServiceAllocationProcessr(transaction.getProcessRegister().getTranType());
        final ServiceAllocationRecord serviceAllocationRequestEntity = processor.buildAllocationRequest(transaction);
        final PaymentResponse response = processor.allocateService(serviceAllocationRequestEntity);
        return response;
    }
}
