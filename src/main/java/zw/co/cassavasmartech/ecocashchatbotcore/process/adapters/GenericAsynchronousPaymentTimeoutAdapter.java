package zw.co.cassavasmartech.ecocashchatbotcore.process.adapters;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.ecocashchatbotcore.common.EBSProcess;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PaymentRecord;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Transaction;
import zw.co.cassavasmartech.ecocashchatbotcore.payment.PaymentResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.payment.PaymentService;
import zw.co.cassavasmartech.ecocashchatbotcore.paymentresponse.PaymentRecordService;
import zw.co.cassavasmartech.ecocashchatbotcore.process.ProcessConstants;
import zw.co.cassavasmartech.ecocashchatbotcore.transaction.TransactionService;


import java.util.Optional;

@Slf4j
@Component
public class GenericAsynchronousPaymentTimeoutAdapter implements JavaDelegate {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private EBSProcess ebsProcess;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private PaymentRecordService paymentRecordService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String paymentReference = (String) delegateExecution.getVariable(ProcessConstants.PAYMENT_REFERENCE);
        Optional<Transaction> transactionLookupResult = transactionService.findByPaymentReference(paymentReference);
        if(transactionLookupResult.isPresent()){
            Transaction transaction=transactionLookupResult.get();
            final PaymentResponse paymentResponse = paymentService.checkTransactionStatus(transaction);
            updateTransaction(transaction, paymentResponse);
            if (paymentResponse.getResponseCode() == (HttpStatus.OK.value())) {
                delegateExecution.setVariable(ProcessConstants.PAYMENT_RESPONSE, paymentResponse);
            } else {
                ebsProcess.stopProcess(delegateExecution, paymentResponse);
            }
        }
    }

    private void updateTransaction(Transaction transaction, PaymentResponse paymentResponse) {

        transaction.setReceiptNumber(paymentResponse.getReceiptNumber());
        transaction.setTranStatus(paymentResponse.getTransactionStatus());
        transaction.setStatusReason(paymentResponse.getResponseMessage());
        transactionService.save(transaction);
        Optional<PaymentRecord> paymentRecordOptional = paymentRecordService.findByTransactionAndTransactionType(transaction, transaction.getPaymentMethod().getTranType());
        if(paymentRecordOptional.isPresent()){
            final PaymentRecord paymentRecord = paymentRecordOptional.get();
            paymentRecord.setTransactionStatus(paymentResponse.getTransactionStatus());
            paymentRecord.setResponseCode(String.valueOf(paymentResponse.getResponseCode()));
            paymentRecord.setResponseMessage(paymentResponse.getResponseMessage());
            paymentRecordService.update(paymentRecord);
        }
    }

}