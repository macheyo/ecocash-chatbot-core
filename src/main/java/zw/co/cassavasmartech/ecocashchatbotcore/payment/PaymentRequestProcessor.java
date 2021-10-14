package zw.co.cassavasmartech.ecocashchatbotcore.payment;

import org.springframework.http.HttpStatus;
import zw.co.cassavasmartech.ecocashchatbotcore.common.enums.TransactionStatus;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Transaction;

public interface PaymentRequestProcessor {

    PaymentResponse processPayment(Transaction transaction);

    default PaymentResponse checkPaymentStatus(Transaction transaction) {
        PaymentResponse paymentResponse = new PaymentResponse(transaction.getPaymentReference());
        paymentResponse.setTransactionStatus(transaction.getTranStatus());
        paymentResponse.setReceiptNumber(transaction.getReceiptNumber());
        paymentResponse.setResponseCode(transaction.getTranStatus().getStatusCode());
        paymentResponse.setResponseMessage(transaction.getStatusReason());
        return paymentResponse;
    }

//    default PaymentResponse reversePayment(Transaction transaction) {
//        throw new BusinessRuntimeException("Transaction Reversal Operation is not supported.");
//    }

    default PaymentResponse buildErrorResponse(String transactionReference, String message) {
        PaymentResponse paymentResponse = new PaymentResponse(transactionReference);
        paymentResponse.setTransactionStatus(TransactionStatus.FAILED);
        paymentResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        paymentResponse.setResponseMessage(message);
        return paymentResponse;
    }
}
