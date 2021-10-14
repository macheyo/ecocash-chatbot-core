package zw.co.cassavasmartech.ecocashchatbotcore.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Partner;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Transaction;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findTransactionBySourceReferenceAndPartner
            (String sourceRef, Partner partner);

    Optional<Transaction> findTransactionBySourceReference(String sourceRef);

    Optional<Transaction> findByPaymentReference(String paymentReference);

}
