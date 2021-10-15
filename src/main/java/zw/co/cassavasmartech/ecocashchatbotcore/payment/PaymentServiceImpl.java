package zw.co.cassavasmartech.ecocashchatbotcore.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.ecocashchatbotcore.common.enums.TransactionStatus;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Transaction;
import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionType;
import zw.co.cassavasmartech.ecocashchatbotcore.transaction.TransactionService;

import java.util.Optional;

@Component
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private PaymentRequestProcessorUtil paymentRequestProcessorUtil;

//    public PaymentResponse processReversal(Transaction transactionToReverse) {
//        final TransactionType tranType = transactionToReverse.getPaymentMethod().getTranType();
//        PaymentRequestProcessor processor = paymentRequestProcessorUtil.getPaymentRequestProcessor(tranType);
//        return processor.reversePayment(transactionToReverse);
//    }

    @Override
    public PaymentResponse checkTransactionStatus(final Transaction transaction) {
        PaymentRequestProcessor paymentRequestProcessor = paymentRequestProcessorUtil.getPaymentRequestProcessor(transaction.getPaymentMethod().getTranType());
        return paymentRequestProcessor.checkPaymentStatus(transaction);
    }

    @Override
    public PaymentResponse processPaymentRequest(PaymentRequest paymentRequest) {
        TransactionType transactionType = paymentRequest.getPaymentMethod().getTranType();
        PaymentRequestProcessor processor = paymentRequestProcessorUtil.getPaymentRequestProcessor(transactionType);
        final Transaction transaction = new TransactionBuilder()
                .tranStatus(TransactionStatus.PENDING)
                .amount(paymentRequest.getAmount())
                .channel(paymentRequest.getRequestChannel())
                .msisdn1(paymentRequest.getSponsoringMsisdn())
                .msisdn2(paymentRequest.getBeneficiaryMsisdn())
                .currency(paymentRequest.getCurrency())
                .paymentMethod(paymentRequest.getPaymentMethod())
                .paymentReference(paymentRequest.getTransactionReference())
                .sourceReference(paymentRequest.getSourceReference())
                .partner(paymentRequest.getPartner())
                .remarks(paymentRequest.getRemarks())
                .channel(paymentRequest.getRequestChannel())
                .callBackUrl(paymentRequest.getCallBackUrl())
                .accountNumber(paymentRequest.getAccountNumber())
                .process(paymentRequest.getProcessRegister())
                .customerName(paymentRequest.getCustomerName())
                .customerEmailAddress(paymentRequest.getCustomerEmailAddress())
                .customerData(paymentRequest.getCustomerData())
                .build();
        final Optional<Transaction> transactionOptional = transactionService.save(transaction);
        final Transaction savedTransaction = transactionOptional.get();
        final PaymentResponse response = processor.processPayment(savedTransaction);
        updateTransactionRecord(response, transaction);
        return response;
    }

    @Override
    public void processPaymentResponse(PaymentResponse paymentResponse) {
        log.info("Processing Payment Response {}", paymentResponse);
        final String paymentReference = paymentResponse.getPaymentReference();
        final Optional<Transaction> optionalTransaction = transactionService.findByPaymentReference(paymentReference);
        if (!optionalTransaction.isPresent()) {
            log.error("Transaction not found for paymentResponse: {}", paymentResponse);
            return;
        }
        final Transaction transaction = optionalTransaction.get();
        updateTransactionRecord(paymentResponse, transaction);
    }


    private void updateTransactionRecord(PaymentResponse paymentResponse, Transaction transaction) {
        transaction.setTranStatus(paymentResponse.getTransactionStatus());
        transaction.setReceiptNumber(paymentResponse.getReceiptNumber());
        transaction.setStatusReason(paymentResponse.getResponseMessage());
        transactionService.save(transaction);
        log.trace("Updating Transaction For Processing Payment Response {}", transaction);
    }

}
