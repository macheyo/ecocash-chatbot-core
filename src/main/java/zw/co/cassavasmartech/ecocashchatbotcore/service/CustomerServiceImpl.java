package zw.co.cassavasmartech.ecocashchatbotcore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.modelAssembler.CustomerModelAssembler;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerModelAssembler assembler;

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> findByCustomerChatId(String id) {
        return customerRepository.findByProfilesChatId(id);
    }

    @Override
    public List<EntityModel<Customer>> findAll() {
        return customerRepository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }
}
