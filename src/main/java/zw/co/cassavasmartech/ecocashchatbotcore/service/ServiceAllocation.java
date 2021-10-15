package zw.co.cassavasmartech.ecocashchatbotcore.service;

import zw.co.cassavasmartech.esb.model.Transaction;
import zw.co.cassavasmartech.esb.payment.PaymentResponse;

public interface ServiceAllocation {

    PaymentResponse processServiceAllocationRequest(Transaction transaction) ;
}
