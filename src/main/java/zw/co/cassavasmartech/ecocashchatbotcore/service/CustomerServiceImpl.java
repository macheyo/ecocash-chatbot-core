package zw.co.cassavasmartech.ecocashchatbotcore.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.PaymentGatewayProcessor;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.data.*;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.EipService;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.EipTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.Merchant;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.MerchantRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.SubscriberToMerchantRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.*;
import zw.co.cassavasmartech.ecocashchatbotcore.model.*;
import zw.co.cassavasmartech.ecocashchatbotcore.modelAssembler.CustomerModelAssembler;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.CustomerRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.selfservice.SelfServiceCoreProcessor;
import zw.co.cassavasmartech.ecocashchatbotcore.statementservice.StatementService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    EipService eipService;

    @Autowired
    SelfServiceCoreProcessor selfServiceCoreProcessor;

    @Autowired
    StatementService statementService;

    @Autowired
    MerchantRepository merchantRepository;

    @Override
    public Customer save(Customer customer) {
        Customer validCustomer = isCustomerValid(customer.getMsisdn());
        if(validCustomer != null) {
            Optional <Customer> c = customerRepository.findByMsisdn(validCustomer.getMsisdn());
            if (c.isPresent())return c.get();
            else return customerRepository.save(modelMapper.map(validCustomer, Customer.class));
        }
        else return validCustomer;
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
    public Optional<Customer> findByMsisdn(String msisdn) {
        return customerRepository.findByMsisdn(msisdn);
    }

    @Override
    public List<EntityModel<Customer>> findAll() {
        return customerRepository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public EnrollmentResponse isEnrolled(String id) {
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
    public Statement getStatement(String chatId, StatementRequest statementRequest) throws ParseException {
        DateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+02:00");
        DateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        Date startDate = isoFormat.parse(statementRequest.getStartDate());
        Date endDate = isoFormat.parse(statementRequest.getEndDate());
        if(startDate.getYear()>today.getYear())startDate.setYear(today.getYear());
        if(endDate.getTime()>today.getYear())endDate.setYear(today.getYear());
        statementRequest.setEndDate(newFormat.format(startDate));
        statementRequest.setStartDate(newFormat.format(endDate));
        Customer customer = customerRepository.findByProfilesChatId(chatId).orElseThrow(() -> new CustomerNotFoundException(chatId));
        statementRequest.setMsisdn(customer.getMsisdn());
        log.info("Statement processor transaction request {}", statementRequest);
        return statementService.getStatement(statementRequest);
    }

    @Override
    public void getStatementFile(String documentId, HttpServletRequest req, HttpServletResponse resp) {
        statementService.getStatementFile(documentId, req, resp);
    }

    @Override
    public Boolean verifyAnswers(String chatId, VerifyAnswerRequest verifyAnswerRequest) {
        Boolean verified=true;
        Customer customer = customerRepository.findByProfilesChatId(chatId).orElseThrow(()->new CustomerNotFoundException(chatId));
        String[] customerAnswers = verifyAnswerRequest.getAnswers().split(",");
        List<Answer> correctAnswerList= selfServiceCoreProcessor.getAnswerByMsisdnAndAnswerStatus(customer.getMsisdn());
        int count=0;
        for(Answer answer:correctAnswerList){
            if(!answer.getAnswer().equalsIgnoreCase(customerAnswers[count]))verified=false;
            count++;
        }
        return verified;
    }

    @Override
    public TransactionResponse customerLookup(SubscriberDto subscriberDto) {
        return paymentGatewayProcessor.lookupCustomer(subscriberDto.getMsisdn());
    }

    @Override
    public TransactionResponse sendMoney(String chatId, SubscriberToSubscriberRequest subscriberToSubscriberRequest) {
        Customer customer = customerRepository.findByProfilesChatId(chatId).orElseThrow(()->new CustomerNotFoundException(chatId));
        subscriberToSubscriberRequest.setMsisdn1(customer.getMsisdn());
        return paymentGatewayProcessor.subscriberToSubscriber(subscriberToSubscriberRequest);
    }

    @Override
    public TransactionResponse billerLookup(BillerLookupRequest billerLookupRequest) {
        return paymentGatewayProcessor.lookupBiller(billerLookupRequest);
    }

    @Override
    public TransactionResponse payBiller(String chatId, SubscriberToBillerRequest subscriberToBillerRequest) {
        Customer customer = customerRepository.findByProfilesChatId(chatId).orElseThrow(() -> new CustomerNotFoundException(chatId));
        subscriberToBillerRequest.setMsisdn(customer.getMsisdn());
        return paymentGatewayProcessor.subscriberToBiller(subscriberToBillerRequest);
    }

    @Override
    public EipTransaction payMerchant(String chatId, SubscriberToMerchantRequest subscriberToMerchantRequest) {
        Customer customer = customerRepository.findByProfilesChatId(chatId).orElseThrow(()->new CustomerNotFoundException(chatId));
        subscriberToMerchantRequest.setMsisdn(customer.getMsisdn());
        return eipService.postPayment(subscriberToMerchantRequest);
    }

    @Override
    public TransactionResponse buyAirtime(String chatId, SubscriberAirtimeRequest subscriberAirtimeRequest) {
        Customer customer = customerRepository.findByProfilesChatId(chatId).orElseThrow(()->new CustomerNotFoundException(chatId));
        subscriberAirtimeRequest.setMsisdn1(customer.getMsisdn());
        return paymentGatewayProcessor.subscriberAirtime(subscriberAirtimeRequest);
    }

    @Override
    public Boolean verifyAnswer(String chatId, VerifyAnswerRequest verifyAnswerRequest) {
        Boolean verified = true;
        String[] answers = verifyAnswerRequest.getAnswers().split(",");
        Customer customer = customerRepository.findByProfilesChatId(chatId).orElseThrow(()->new CustomerNotFoundException(chatId));
        List<Answer> answerList = selfServiceCoreProcessor.getAnswerByMsisdnAndAnswerStatus(customer.getMsisdn());
        int count = 0;
        for (Answer answer: answerList){
            log.info("This is the answer provided: {} and the correct answe {}", answer.getAnswer(), answers[count]);
            if(!answer.getAnswer().equals(answers[count]))verified = false;
            count++;
        }
        return verified;
    }

    @Override
    public TransactionResponse registerCustomer(Registration registration) {
        return paymentGatewayProcessor.registerCustomer(registration);
    }

    @Override
    public EipTransaction payMerchant2(String chatId, SubscriberToMerchantRequest subscriberToMerchantRequest) {
        Merchant merchant = merchantRepository.findByName(subscriberToMerchantRequest.getMerchantName()).orElseThrow(()->new MerchantNotFoundException(subscriberToMerchantRequest.getMerchantName()));
        Customer customer = customerRepository.findByProfilesChatId(chatId).orElseThrow(()->new CustomerNotFoundException(chatId));
        subscriberToMerchantRequest.setMsisdn(customer.getMsisdn());
        subscriberToMerchantRequest.setMerchantCode(merchant.getMerchantCode());
        return eipService.postPayment(subscriberToMerchantRequest);
    }

    private Customer isCustomerValid(String msisdn){
        Customer customer = new Customer();
        TransactionResponse transactionResponse = paymentGatewayProcessor.lookupCustomer(msisdn);
        log.info("Cpg transaction response {}", transactionResponse);
        if(transactionResponse==null && transactionResponse.getField1()==null)throw new BusinessException("Null response from CPG");
        if(transactionResponse.getField1().equalsIgnoreCase(String.valueOf(HttpStatus.OK.value()))
                && transactionResponse.getField7().equals("Active")){
            String customerName [] = transactionResponse.getField6().split(" ");
            customer.setFirstName(customerName[0]);
            customer.setLastName(customerName[customerName.length-1]);
            customer.setDob("");
            customer.setMsisdn(msisdn);
            customer.setNatId("");
        }
        else customer = null;
        return customer;
    }


}
