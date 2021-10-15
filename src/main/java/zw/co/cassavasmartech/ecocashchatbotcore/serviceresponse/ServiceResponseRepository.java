package zw.co.cassavasmartech.ecocashchatbotcore.serviceresponse;

import org.springframework.data.jpa.repository.JpaRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.model.ServiceAllocationRecord;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Transaction;


import java.util.Optional;

public interface ServiceResponseRepository extends JpaRepository<ServiceAllocationRecord, Long> {

    Optional<ServiceAllocationRecord> findByTransaction(Transaction transaction);

}
