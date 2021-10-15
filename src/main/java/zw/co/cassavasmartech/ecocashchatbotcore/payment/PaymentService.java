package zw.co.cassavasmartech.ecocashchatbotcore.payment;


import zw.co.cassavasmartech.esb.model.Transaction;

public interface PaymentService {

    PaymentResponse processPaymentRequest(PaymentRequest paymentRequest);

    //PaymentResponse processReversal(Transaction transaction);

    PaymentResponse checkTransactionStatus(final Transaction transaction);

    void processPaymentResponse(PaymentResponse paymentResponse);

}
