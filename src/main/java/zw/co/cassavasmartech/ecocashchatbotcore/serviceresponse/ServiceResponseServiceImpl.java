package zw.co.cassavasmartech.ecocashchatbotcore.serviceresponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.model.ServiceAllocationRecord;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Transaction;

import java.util.Optional;

@Service
public class ServiceResponseServiceImpl implements ServiceResponseService{

    @Autowired
    private ServiceResponseRepository repository;

    @Override
    public Optional<ServiceAllocationRecord> save(ServiceAllocationRecord serviceResponse) {
        return Optional.ofNullable(repository.save(serviceResponse));
    }

    @Override
    public Optional<ServiceAllocationRecord> findByTransaction(Transaction transaction){
        return repository.findByTransaction(transaction);
    }

    @Override
    public Optional<ServiceAllocationRecord> update(ServiceAllocationRecord serviceResponse) {
        return this.save(serviceResponse);
    }
}
