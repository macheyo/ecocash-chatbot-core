package zw.co.cassavasmartech.ecocashchatbotcore.service;

import javafx.util.converter.LocalDateTimeStringConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.common.MessagePropertiesService;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.PaymentGatewayProcessor;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.BusinessException;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.CustomerNotFoundException;
import zw.co.cassavasmartech.ecocashchatbotcore.model.*;
import zw.co.cassavasmartech.ecocashchatbotcore.modelAssembler.CustomerModelAssembler;
import zw.co.cassavasmartech.ecocashchatbotcore.notification.NotificationService;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.CustomerRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.ProfileRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.selfServiceCore.SelfServiceCoreProcessor;
import zw.co.cassavasmartech.ecocashchatbotcore.sms.Sms;
import zw.co.cassavasmartech.ecocashchatbotcore.sms.SmsDispatchStrategy;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static zw.co.cassavasmartech.ecocashchatbotcore.common.Util.buildJsonHttpHeaders;

@Service
@Slf4j
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    MessagePropertiesService messagePropertiesService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    CustomerModelAssembler assembler;

    @Autowired
    PaymentGatewayProcessor paymentGatewayProcessor;

    @Autowired
    SelfServiceCoreProcessor selfServiceCoreProcessor;

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
        if(transactionResponse==null && transactionResponse.getField1()==null)throw new BusinessException("Null response from CPG");
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
        Customer customer = customerRepository.findByProfilesChatId(id).orElseThrow(()->new CustomerNotFoundException(id));
        Profile profile = customer.getProfiles().get(customer.getProfiles().indexOf(profileRepository.getByChatId(id)));
        profile.setOtp(createOtp());
        profileRepository.save(profile);
        return sendSms(customer.getMsisdn(), profile.getOtp().getVerificationCode());
    }

    @Override
    public Boolean isEnrolled(String id) {
        Customer customer = customerRepository.findByProfilesChatId(id).orElseThrow(()->new CustomerNotFoundException(id));
        return selfServiceCoreProcessor.isEnrolled(customer.getMsisdn());
    }

    @Override
    public Boolean verifyCustomer(String id, String verificationCode) {
        Customer customer = customerRepository.findByProfilesChatId(id).orElseThrow(()->new CustomerNotFoundException(id));
        Profile profile = customer.getProfiles().get(customer.getProfiles().indexOf(profileRepository.getByChatId(id)));
        if(profile.getOtp().getVerificationCode().equalsIgnoreCase(verificationCode) && !isExpired(profile.getCreatedDate()))
        return true;
        else return false;
    }

    private Boolean isExpired(LocalDateTime created_date){
        Duration duration = Duration.between(created_date, LocalDateTime.now());
        if(duration.toMinutes()>2) return false;
        return true;
    }

    private OTP createOtp() {
        OTP otp = new OTP();
        otp.setVerificationCode(String.valueOf(new Random().nextInt(900000) + 100000));
        return otp;
    }


    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
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

    private Boolean sendSms(String msisdn, String verificationCode) {
        final String notificationMessage = String.format(messagePropertiesService.getByKey("messages.otp"), verificationCode);
        final Sms sms = new Sms(msisdn, notificationMessage);
        notificationService.sendSms(sms, SmsDispatchStrategy.DISPATCH_NOW);
        return true;
    }
}
