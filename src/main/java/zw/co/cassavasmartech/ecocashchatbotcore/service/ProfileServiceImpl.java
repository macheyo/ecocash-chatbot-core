package zw.co.cassavasmartech.ecocashchatbotcore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.common.MessagePropertiesService;
import zw.co.cassavasmartech.ecocashchatbotcore.common.MobileNumberFormater;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.CustomerAlreadyExistsException;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.CustomerNotFoundException;
import zw.co.cassavasmartech.ecocashchatbotcore.invoker.CoreInvoker;
import zw.co.cassavasmartech.ecocashchatbotcore.model.OTP;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Profile;
import zw.co.cassavasmartech.ecocashchatbotcore.modelAssembler.ProfileModelAssembler;
import zw.co.cassavasmartech.ecocashchatbotcore.notification.NotificationService;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.CustomerRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.ProfileRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.sms.Sms;
import zw.co.cassavasmartech.ecocashchatbotcore.sms.SmsProperties;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService{
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    ProfileModelAssembler assembler;

    @Autowired
    MessagePropertiesService messagePropertiesService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    SmsProperties smsProperties;

    @Autowired
    MobileNumberFormater mobileNumberFormater;

    @Autowired
    CoreInvoker coreInvoker;

    @Override
    public Profile save(String msisdn, Profile profile) {
        if(profileRepository.getByChatId(profile.getChatId()).isPresent()) throw new CustomerAlreadyExistsException(profile.getChatId());
        return customerRepository.findByMsisdn(msisdn).map(customer -> {
            profile.setCustomer(customer);
            log.info("new profile created {}", profile.getChatId());
            return profileRepository.save(profile);
        }).orElseThrow(()->new CustomerNotFoundException(msisdn));
    }

    @Override
    public List<EntityModel<Profile>> findAll() {
        return profileRepository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void generateOtp(String chatId) {
       Profile profile = profileRepository.getByChatId(chatId).orElseThrow(()->new CustomerNotFoundException(chatId));
       profile.setOtp(createOtp());
       profileRepository.save(profile);
       sendSms(profile.getCustomer().getMsisdn(),profile.getOtp().getVerificationCode());
    }

    @Override
    public Boolean verifyCustomer(String chatId, String verificationCode) {
        Profile profile = profileRepository.getByChatId(chatId).orElseThrow(()->new CustomerNotFoundException(chatId));
        return isExpired(profile.getOtp().getCreatedDate());
    }

    private Boolean isExpired(LocalDateTime created_date){
        LocalDateTime localDateTime = LocalDateTime.now();
        Duration duration = Duration.between(created_date, localDateTime);
        Long minute = duration.toMinutes();
        if(minute>2L) return true;
        return false;
    }

    private OTP createOtp() {
        OTP otp = new OTP();
        otp.setVerificationCode(String.valueOf(new Random().nextInt(900000) + 100000));
        return otp;
    }

    private void sendSms(String msisdn, String verificationCode) {
        final String notificationMessage = String.format(messagePropertiesService.getByKey("messages.otp"), verificationCode);
        coreInvoker.invoke(Sms.builder().to(mobileNumberFormater.formatMobileNumberInternational(msisdn)).text(notificationMessage).build(),
                smsProperties.getEndPointUrl(),
                HttpMethod.POST,
                new ParameterizedTypeReference <ApiResponse<Sms>>() {
                });
    }

}
