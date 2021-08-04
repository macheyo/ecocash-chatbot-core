package zw.co.cassavasmartech.ecocashchatbotcore.service;

import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.model.*;

import java.util.List;
import java.util.Optional;

@Service
public interface CustomerService {
    Customer save(Customer customer);
    Optional<Customer> findByCustomerChatId(String id);
    List<EntityModel<Customer>> findAll();
    Optional<Customer> update(String chatId, Customer customer);
    Boolean generateOtp(String id);
    Boolean verifyCustomer(String id, String verificationCode);
    Optional<Customer> findById(Long id);
}
