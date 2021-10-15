package zw.co.cassavasmartech.ecocashchatbotcore.process.adapters;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.ecocashchatbotcore.common.EBSProcess;
import zw.co.cassavasmartech.ecocashchatbotcore.common.enums.TransactionStatus;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Transaction;
import zw.co.cassavasmartech.ecocashchatbotcore.payment.PaymentResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.process.ProcessConstants;
import zw.co.cassavasmartech.ecocashchatbotcore.service.ServiceAllocation;
import zw.co.cassavasmartech.ecocashchatbotcore.transaction.TransactionService;

import java.util.Optional;

@Slf4j
@Component
public class GenericServiceAllocatorAdapter implements JavaDelegate {

    @Autowired
    private ServiceAllocation serviceAllocationService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private EBSProcess ebsProcess;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        String paymentReference = (String) delegateExecution.getVariable(ProcessConstants.PAYMENT_REFERENCE);
        Optional<Transaction> transactionLookupResult = transactionService.findByPaymentReference(paymentReference);
        transactionLookupResult.ifPresent(transaction -> {

            if (transaction.getTranStatus() != TransactionStatus.SUCCESS) {
                processFailedTransaction(delegateExecution, transaction);
            } else {
                processSuccessfulTransaction(delegateExecution, transaction);
            }
        });
    }

    private void processSuccessfulTransaction(DelegateExecution delegateExecution, Transaction transaction) {
        PaymentResponse paymentResponse = serviceAllocationService.processServiceAllocationRequest(transaction);
        paymentResponse.setReceiptNumber(transaction.getReceiptNumber());
        if (paymentResponse.getTransactionStatus() == TransactionStatus.SUCCESS) {
            delegateExecution.setVariable(ProcessConstants.PAYMENT_RESPONSE, paymentResponse);
        } else if (paymentResponse.getTransactionStatus() == TransactionStatus.PENDING_REVERSAL ||
                paymentResponse.getTransactionStatus() == TransactionStatus.FAILED) {
            delegateExecution.setVariable(ProcessConstants.PAYMENT_RESPONSE, paymentResponse);
            delegateExecution.setVariable(ProcessConstants.VAR_NAME_REFUND_TRIGGER, true);
        } else if (paymentResponse.getTransactionStatus() == TransactionStatus.PENDING_TOKEN_CHECK ||
                paymentResponse.getTransactionStatus() == TransactionStatus.TIMEOUT ||
                paymentResponse.getTransactionStatus() == TransactionStatus.PENDING_MANUAL_REVERSAL) {
            delegateExecution.setVariable(ProcessConstants.PAYMENT_RESPONSE, paymentResponse);
            delegateExecution.setVariable(ProcessConstants.VAR_NAME_CALLBACK_TRIGGER, true);
        } else {
            log.info("Cancelling process because of invalid transaction status");
            ebsProcess.stopProcess(delegateExecution, paymentResponse);
        }
    }

    private void processFailedTransaction(DelegateExecution delegateExecution, Transaction transaction) {
        log.info("Transaction failed. {}, {}", transaction.getPaymentReference(), transaction.getTranStatus());

        final PaymentResponse paymentResponse = PaymentResponse.builder()
                .callBackUrl(transaction.getCallBackUrl())
                .paymentReference(transaction.getPaymentReference())
                .responseCode(transaction.getTranStatus().getStatusCode())
                .responseMessage(transaction.getStatusReason())
                .accountNumber(transaction.getAccount_number())
                .receiptNumber(transaction.getReceiptNumber())
                .subscriberMsisdn(transaction.getMsisdn1())
                .transactionStatus(transaction.getTranStatus())
                .build();

        ebsProcess.stopProcess(delegateExecution, paymentResponse);
    }
}