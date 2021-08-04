package zw.co.cassavasmartech.ecocashchatbotcore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.PaymentGatewayProcessor;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.BusinessException;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.CustomerNotFoundException;
import zw.co.cassavasmartech.ecocashchatbotcore.model.*;
import zw.co.cassavasmartech.ecocashchatbotcore.modelAssembler.CustomerModelAssembler;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.CustomerRepository;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static zw.co.cassavasmartech.ecocashchatbotcore.common.Util.buildJsonHttpHeaders;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    CustomerModelAssembler assembler;

    @Autowired
    PaymentGatewayProcessor paymentGatewayProcessor;

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

    @Override
    public Optional<Customer> update(String chatId, Customer customer) {
        Customer customerToUpdate = customerRepository.findByProfilesChatId(chatId).orElseThrow(()->new CustomerNotFoundException(chatId));
        TransactionResponse transactionResponse = paymentGatewayProcessor.lookupCustomer(customer.getMsisdn());
        log.info("Cpg transaction response {}", transactionResponse);
        if(transactionResponse!=null && transactionResponse.getField1()!=null)throw new BusinessException("Null response from CPG");
        if(transactionResponse.getField1().equalsIgnoreCase(String.valueOf(HttpStatus.OK.value()))
                && transactionResponse.getField7().equals("Y")){
            customerToUpdate.setFirstName(transactionResponse.getField6());
            customerToUpdate.setLastName(transactionResponse.getField9());
            customerToUpdate.setDob(transactionResponse.getField11());
            customerToUpdate.setMsisdn(transactionResponse.getField10());
            customerToUpdate.setNatId(transactionResponse.getField12());
            customerRepository.save(customerToUpdate);
        }
        else customerToUpdate = null;
        return Optional.ofNullable(customerToUpdate);
    }

    @Override
    public Boolean generateOtp(String id) {
        Optional<Customer> customer = customerRepository.findByProfilesChatId(id);
        if(customer.isPresent()){
            Customer myCustomer = customer.get();
            List<Profile> profiles = myCustomer.getProfiles();
            //profiles.indexOf();

            sendSms(myCustomer.getMsisdn());
        }
        return false;
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    private void sendSms(String msisdn) {

    }
}
