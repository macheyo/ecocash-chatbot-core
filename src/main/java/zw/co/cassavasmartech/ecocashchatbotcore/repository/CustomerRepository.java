package zw.co.cassavasmartech.ecocashchatbotcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    //Optional<Customer> findByProfilesChatId(String id);

    Optional<Customer> findByMsisdn(String msisdn);
    Optional<Customer> findByProfilesChatId(String chatId);
}
