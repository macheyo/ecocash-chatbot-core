package zw.co.cassavasmartech.ecocashchatbotcore.paymentresponse;

import zw.co.cassavasmartech.ecocashchatbotcore.model.PaymentRecord;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Transaction;
import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionType;

import java.util.List;
import java.util.Optional;

public interface PaymentRecordService {

    Optional<PaymentRecord> save(PaymentRecord paymentRecord);

    Optional<List<PaymentRecord>> findByTransaction(Transaction transaction);

    Optional<PaymentRecord> update(PaymentRecord paymentRecord);

    Optional<PaymentRecord> findByRequestReference(String requestRef);

    Optional<PaymentRecord> findByTransactionAndTransactionType(Transaction transaction, TransactionType transactionType);

}
