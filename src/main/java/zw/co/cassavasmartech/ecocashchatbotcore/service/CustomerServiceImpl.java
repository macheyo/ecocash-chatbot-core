package zw.co.cassavasmartech.ecocashchatbotcore.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.PaymentGatewayProcessor;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.*;
import zw.co.cassavasmartech.ecocashchatbotcore.model.*;
import zw.co.cassavasmartech.ecocashchatbotcore.modelAssembler.CustomerModelAssembler;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.CustomerRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.selfServiceCore.SelfServiceCoreProcessor;
import zw.co.cassavasmartech.ecocashchatbotcore.statementProcessor.StatementProcessor;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerModelAssembler assembler;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PaymentGatewayProcessor paymentGatewayProcessor;

    @Autowired
    SelfServiceCoreProcessor selfServiceCoreProcessor;

    @Autowired
    StatementProcessor statementProcessor;

    @Override
    public Customer save(Customer customer) {
        if (customerRepository.findByMsisdn(customer.getMsisdn()).isPresent())throw new CustomerAlreadyExistsException(customer.getMsisdn());
        Customer validCustomer = isCustomerValid(customer.getMsisdn());
        if(validCustomer != null) return customerRepository.save(modelMapper.map(validCustomer,Customer.class));
        else throw new CustomerNotValidException(customer.getMsisdn());

    }

    @Override
    public Customer getByChatId(String chatId) {
        return customerRepository.findByProfilesChatId(chatId)
                .orElseThrow(()->new CustomerNotFoundException(chatId));
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public List<EntityModel<Customer>> findAll() {
        return customerRepository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean isEnrolled(String id) {
        Customer customer = customerRepository.findByProfilesChatId(id).orElseThrow(()->new CustomerNotFoundException(id));
        return selfServiceCoreProcessor.isEnrolled(customer.getMsisdn());
    }

    @Override
    public List<Answer> getAnswers(String chatId) {
        Customer customer = customerRepository.findByProfilesChatId(chatId).orElseThrow(()->new CustomerNotFoundException(chatId));
        return selfServiceCoreProcessor.getAnswerByMsisdnAndAnswerStatus(customer.getMsisdn());
    }

    @Override
    public SubscriberDto getAlternative(String chatId) {
        Customer customer = customerRepository.findByProfilesChatId(chatId).orElseThrow(()->new CustomerNotFoundException(chatId));
        return selfServiceCoreProcessor.getAlternative(customer.getMsisdn());
    }
    @Override
    public Boolean pinReset(String chatId) {
        Customer customer = customerRepository.findByProfilesChatId(chatId).orElseThrow(()->new CustomerNotFoundException(chatId));
        TransactionResponse response = paymentGatewayProcessor.pinReset(customer.getMsisdn());
        if(response!=null&&response.getField1()!=null) {
            if (response.getField1().equalsIgnoreCase(String.valueOf(HttpStatus.OK.value()))) return true;
        }
        return false;
    }

    @Override
    public Statement getStatement(String chatId, StatementRequest statementRequest) {
        Customer customer = customerRepository.findByProfilesChatId(chatId).orElseThrow(()->new CustomerNotFoundException(chatId));
        statementRequest.setMsisdn(customer.getMsisdn());
        return statementProcessor.getStatement(statementRequest);
    }



    private Customer isCustomerValid(String msisdn){
        Customer customer = new Customer();
        TransactionResponse transactionResponse = paymentGatewayProcessor.lookupCustomer(msisdn);
        log.info("Cpg transaction response {}", transactionResponse);
        if(transactionResponse==null && transactionResponse.getField1()==null)throw new BusinessException("Null response from CPG");
        if(transactionResponse.getField1().equalsIgnoreCase(String.valueOf(HttpStatus.OK.value()))
                && transactionResponse.getField7().equals("Y")){
            customer.setFirstName(transactionResponse.getField6());
            customer.setLastName(transactionResponse.getField9());
            customer.setDob(transactionResponse.getField11());
            customer.setMsisdn(transactionResponse.getField10());
            customer.setNatId(transactionResponse.getField12());
        }
        else customer = null;
        return customer;
    }


}
