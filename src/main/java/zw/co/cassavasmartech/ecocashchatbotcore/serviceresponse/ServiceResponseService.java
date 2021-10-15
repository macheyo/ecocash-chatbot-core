package zw.co.cassavasmartech.ecocashchatbotcore.serviceresponse;



import zw.co.cassavasmartech.ecocashchatbotcore.model.ServiceAllocationRecord;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Transaction;

import java.util.Optional;

public interface ServiceResponseService {

    Optional<ServiceAllocationRecord> save(ServiceAllocationRecord serviceResponse);

    Optional<ServiceAllocationRecord> findByTransaction(Transaction transaction);

    Optional<ServiceAllocationRecord> update(ServiceAllocationRecord serviceResponse);

}
