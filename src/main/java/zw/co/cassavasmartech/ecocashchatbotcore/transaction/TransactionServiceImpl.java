package zw.co.cassavasmartech.ecocashchatbotcore.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Partner;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Transaction;

import java.util.Optional;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Optional<Transaction> save(Transaction transaction) {
        return Optional.ofNullable(transactionRepository.save(transaction));
    }

    @Override
    public Optional<Transaction> update(Transaction transaction) {
        return this.save(transaction);
    }

    @Override
    public Optional<Transaction> findBySourceReference(String sourceReference) {
        return transactionRepository.findTransactionBySourceReference(sourceReference);
    }

    @Override
    public Optional<Transaction> findBySourceReferenceAndPartner(String sourceReference, Partner partner) {
        return transactionRepository.findTransactionBySourceReferenceAndPartner(sourceReference,partner);
    }

    @Override
    public Optional<Transaction> findByPaymentReference(String paymentReference) {
        return transactionRepository.findByPaymentReference(paymentReference);
    }

}
