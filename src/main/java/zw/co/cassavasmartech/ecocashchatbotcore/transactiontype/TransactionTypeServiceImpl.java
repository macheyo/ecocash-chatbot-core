package zw.co.cassavasmartech.ecocashchatbotcore.transactiontype;

import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.esb.model.PaymentMethod;
import zw.co.cassavasmartech.esb.model.TransactionType;

import java.util.Optional;

@Service
public class TransactionTypeServiceImpl implements TransactionTypeService {

    private final TransactionTypeRepository transactionTypeRepository;

    public TransactionTypeServiceImpl(TransactionTypeRepository transactionTypeRepository) {
        this.transactionTypeRepository = transactionTypeRepository;
    }

    @Override
    public Optional<TransactionType> save(TransactionType transactionType) {
        return Optional.ofNullable(transactionTypeRepository.save(transactionType));
    }

    @Override
    public Optional<TransactionType> update(TransactionType transactionType) {
        return Optional.ofNullable(transactionTypeRepository.save(transactionType));
    }

    public Optional<TransactionType> findByPaymentMethod(PaymentMethod paymentMethod) {
        return Optional.empty();
    }
}
