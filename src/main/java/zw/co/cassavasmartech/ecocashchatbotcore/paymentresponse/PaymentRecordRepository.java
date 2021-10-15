package zw.co.cassavasmartech.ecocashchatbotcore.paymentresponse;

import org.springframework.data.jpa.repository.JpaRepository;

import zw.co.cassavasmartech.ecocashchatbotcore.model.PaymentRecord;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Transaction;
import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionType;

import java.util.List;
import java.util.Optional;

public interface PaymentRecordRepository extends JpaRepository<PaymentRecord, Long> {
    Optional<List<PaymentRecord>> findAllByTransaction(Transaction transaction);

    Optional<PaymentRecord> findByRequestReference(String requestRef);

    Optional<PaymentRecord> findByTransactionAndTransactionType(Transaction transaction, TransactionType transactionType);
}
