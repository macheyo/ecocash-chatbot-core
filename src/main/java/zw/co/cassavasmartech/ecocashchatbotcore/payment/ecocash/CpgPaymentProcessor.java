package zw.co.cassavasmartech.ecocashchatbotcore.payment.ecocash;

import org.springframework.http.HttpStatus;
import zw.co.cassavasmartech.ecocashchatbotcore.common.enums.TransactionStatus;
import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.payment.PaymentResponse;

public interface CpgPaymentProcessor {

    default PaymentResponse buildPaymentResponseFromCpgResponse(TransactionResponse cpgResponse, String transactionReference) {
        boolean isSuccess = String.valueOf(HttpStatus.OK.value()).equalsIgnoreCase(cpgResponse.getField1());
        PaymentResponse paymentResponse = new PaymentResponse(transactionReference);
        paymentResponse.setTransactionStatus(isSuccess ? TransactionStatus.SUCCESS : TransactionStatus.FAILED);
        paymentResponse.setResponseCode(Integer.parseInt(cpgResponse.getField1()));
        paymentResponse.setResponseMessage(cpgResponse.getField2());
        paymentResponse.setReceiptNumber(cpgResponse.getField3());
        return paymentResponse;
    }
}


