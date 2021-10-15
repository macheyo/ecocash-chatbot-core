package zw.co.cassavasmartech.ecocashchatbotcore.transactiontype;

import zw.co.cassavasmartech.ecocashchatbotcore.model.PaymentMethod;
import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionType;

import java.util.Optional;

public interface TransactionTypeService {

    Optional<TransactionType> save(TransactionType transactionType);

    Optional<TransactionType> update(TransactionType transactionType);

    Optional<TransactionType> findByPaymentMethod(PaymentMethod paymentMethod);
}
