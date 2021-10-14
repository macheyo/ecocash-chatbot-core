package zw.co.cassavasmartech.ecocashchatbotcore.process.adapters;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.esb.commons.enums.TransactionStatus;
import zw.co.cassavasmartech.esb.model.Transaction;
import zw.co.cassavasmartech.esb.payment.*;
import zw.co.cassavasmartech.esb.process.ProcessConstants;
import zw.co.cassavasmartech.esb.transaction.TransactionService;

import java.util.Optional;

@Slf4j
@Component
public class GenericRefundAdapter implements JavaDelegate {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private PaymentRequestProcessorUtil paymentRequestProcessorUtil;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String paymentReference = (String) delegateExecution.getVariable(ProcessConstants.PAYMENT_REFERENCE);
        String callbackUrl = (String) delegateExecution.getVariable(ProcessConstants.VAR_NAME_CALL_BACK_URL);
        Optional<Transaction> transactionLookupResult = transactionService.findByPaymentReference(paymentReference);
        if (transactionLookupResult.isPresent()) {
            Transaction transaction = transactionLookupResult.get();
            PaymentRequestProcessor paymentRequestProcessor = paymentRequestProcessorUtil.getPaymentRequestProcessor(transaction.getPaymentMethod().getTranType());
            //final PaymentResponse paymentResponse = paymentRequestProcessor.reversePayment(transaction);
            final PaymentResponse paymentResponse = buildFailedReversalPaymentResponse(paymentReference);
            paymentResponse.setCallBackUrl(callbackUrl);
            delegateExecution.setVariable(ProcessConstants.PAYMENT_RESPONSE, paymentResponse);
        }
    }

    private PaymentResponse buildFailedReversalPaymentResponse(String transactionReference) {

        PaymentResponse paymentResponse = new PaymentResponse(transactionReference);
        paymentResponse.setTransactionStatus(TransactionStatus.PENDING_MANUAL_REVERSAL);
        paymentResponse.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        paymentResponse.setResponseMessage("Failed reversal");
        return paymentResponse;

    }
}