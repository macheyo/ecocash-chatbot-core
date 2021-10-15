package zw.co.cassavasmartech.ecocashchatbotcore.service;


import zw.co.cassavasmartech.ecocashchatbotcore.model.Transaction;
import zw.co.cassavasmartech.ecocashchatbotcore.payment.PaymentResponse;

public interface ServiceAllocation {

    PaymentResponse processServiceAllocationRequest(Transaction transaction) ;
}
