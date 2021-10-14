package zw.co.cassavasmartech.ecocashchatbotcore.transaction;


import zw.co.cassavasmartech.ecocashchatbotcore.model.Partner;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Transaction;

import java.util.Optional;

public interface TransactionService {

    Optional<Transaction> save(Transaction transaction);

    Optional<Transaction> update(Transaction transaction);

    Optional<Transaction> findBySourceReference(String sourceReference);

    Optional<Transaction> findBySourceReferenceAndPartner(String sourceReference, Partner partner);

    Optional<Transaction> findByPaymentReference(String paymentReference);

}
