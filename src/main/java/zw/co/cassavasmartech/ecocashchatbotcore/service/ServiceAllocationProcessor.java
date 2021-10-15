package zw.co.cassavasmartech.ecocashchatbotcore.service;

import zw.co.cassavasmartech.ecocashchatbotcore.model.ServiceAllocationRecord;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Transaction;
import zw.co.cassavasmartech.ecocashchatbotcore.payment.PaymentResponse;

public interface ServiceAllocationProcessor {

    ServiceAllocationRecord buildAllocationRequest(Transaction transaction);

    PaymentResponse allocateService(ServiceAllocationRecord transaction);

}
