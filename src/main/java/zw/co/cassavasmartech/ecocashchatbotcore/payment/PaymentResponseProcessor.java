package zw.co.cassavasmartech.ecocashchatbotcore.payment;

import org.springframework.http.HttpStatus;
import zw.co.cassavasmartech.esb.commons.enums.TransactionStatus;
import zw.co.cassavasmartech.esb.model.Transaction;

public interface PaymentResponseProcessor {

    PaymentResponse processResponse(Transaction transaction);

    default PaymentResponse buildErrorResponse(Transaction transaction, String message) {
        PaymentResponse paymentResponse = new PaymentResponse(transaction.getPaymentReference());
        paymentResponse.setTransactionStatus(TransactionStatus.FAILED);
        paymentResponse.setResponseCode((HttpStatus.NOT_FOUND.value()));
        paymentResponse.setResponseMessage(message);
        return paymentResponse;
    }

    default PaymentResponse buildSuccessResponse(Transaction transaction) {
        PaymentResponse paymentResponse = new PaymentResponse(transaction.getPaymentReference());
        paymentResponse.setTransactionStatus(TransactionStatus.SUCCESS);
        paymentResponse.setResponseCode(HttpStatus.OK.value());
        return paymentResponse;
    }
}
