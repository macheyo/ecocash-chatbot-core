package zw.co.cassavasmartech.ecocashchatbotcore.paymentresponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PaymentRecord;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Transaction;
import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionType;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentRecordServiceImpl implements PaymentRecordService{

    @Autowired
    private PaymentRecordRepository repository;

    @Override
    public Optional<PaymentRecord> save(PaymentRecord serviceResponse) {
        return Optional.ofNullable(repository.save(serviceResponse));
    }

    @Override
    public Optional<List<PaymentRecord>> findByTransaction(Transaction transaction){
        return repository.findAllByTransaction(transaction);
    }

    @Override
    public Optional<PaymentRecord> update(PaymentRecord serviceResponse) {
        return this.save(serviceResponse);
    }

    @Override
    public Optional<PaymentRecord> findByRequestReference(String requestRef) {
        return repository.findByRequestReference(requestRef);
    }

    @Override
    public Optional<PaymentRecord> findByTransactionAndTransactionType(Transaction transaction, TransactionType transactionType) {
        return repository.findByTransactionAndTransactionType(transaction,transactionType);
    }
}
