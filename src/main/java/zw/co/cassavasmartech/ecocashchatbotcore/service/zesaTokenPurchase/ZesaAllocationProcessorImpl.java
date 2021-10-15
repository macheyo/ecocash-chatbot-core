package zw.co.cassavasmartech.ecocashchatbotcore.service.zesaTokenPurchase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.common.enums.ServiceType;
import zw.co.cassavasmartech.ecocashchatbotcore.common.enums.TransactionStatus;
import zw.co.cassavasmartech.ecocashchatbotcore.model.ServiceAllocationRecord;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Transaction;
import zw.co.cassavasmartech.ecocashchatbotcore.payment.PaymentResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.service.ServiceAllocationProcessor;
import zw.co.cassavasmartech.ecocashchatbotcore.serviceresponse.ServiceResponseService;

import java.util.Optional;

@Service
public class ZesaAllocationProcessorImpl implements ServiceAllocationProcessor {

    @Autowired
    private ZesaService zesaService;
    @Autowired
    private ServiceResponseService responseService;

    @Override
    public PaymentResponse allocateService(ServiceAllocationRecord allocationRecord) {
        return zesaService.postPayment(allocationRecord);
    }

    @Override
    public ServiceAllocationRecord buildAllocationRequest(Transaction transaction) {
        ServiceAllocationRecord allocationRecord=new ServiceAllocationRecord();
        allocationRecord.setTransaction(transaction);
        allocationRecord.setServiceType(ServiceType.ZESA_PREPAID);
        allocationRecord.setTransactionStatus(TransactionStatus.PENDING);
        final Optional<ServiceAllocationRecord> allocationRecordOptional = responseService.save(allocationRecord);
        return allocationRecordOptional.orElse(null);
    }
}
