package zw.co.cassavasmartech.ecocashchatbotcore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.common.MessagePropertiesService;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.CustomerAlreadyExistsException;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.CustomerNotFoundException;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.CustomerVerificationFailedException;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.OTP;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Profile;
import zw.co.cassavasmartech.ecocashchatbotcore.modelAssembler.ProfileModelAssembler;
import zw.co.cassavasmartech.ecocashchatbotcore.notification.NotificationService;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.CustomerRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.ProfileRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.sms.Sms;
import zw.co.cassavasmartech.ecocashchatbotcore.sms.SmsDispatchStrategy;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
@Service
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

    @Override
    public Profile save(String msisdn, Profile profile) {
        if(profileRepository.getByChatId(profile.getChatId()).isPresent()) throw new CustomerAlreadyExistsException(profile.getChatId());
        return customerRepository.findByMsisdn(msisdn).map(customer -> {
            profile.setCustomer(customer);
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
    public Boolean generateOtp(String chatId) {
       Profile profile = profileRepository.getByChatId(chatId).orElseThrow(()->new CustomerNotFoundException(chatId));
       profile.setOtp(createOtp());
       profileRepository.save(profile);
       return sendSms(profile.getCustomer().getMsisdn(),profile.getOtp().getVerificationCode());
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

    private Boolean sendSms(String msisdn, String verificationCode) {
        final String notificationMessage = String.format(messagePropertiesService.getByKey("messages.otp"), verificationCode);
        final Sms sms = new Sms(msisdn, notificationMessage);
        notificationService.sendSms(sms, SmsDispatchStrategy.DISPATCH_NOW);
        return true;
    }

}
