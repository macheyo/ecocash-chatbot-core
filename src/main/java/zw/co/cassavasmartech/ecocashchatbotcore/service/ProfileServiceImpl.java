package zw.co.cassavasmartech.ecocashchatbotcore.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.CustomerAlreadyExistsException;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.CustomerNotFoundException;
import zw.co.cassavasmartech.ecocashchatbotcore.model.OTP;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Profile;
import zw.co.cassavasmartech.ecocashchatbotcore.modelAssembler.ProfileModelAssembler;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.CustomerRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.repository.ProfileRepository;
import zw.co.cassavasmartech.ecocashchatbotcore.sms.SmsService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    SmsService smsService;

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
       smsService.sendSms(profile.getCustomer().getMsisdn(),profile.getOtp().getVerificationCode());
    }

    @Override
    public Boolean verifyCustomer(String chatId, String verificationCode) {
        Profile profile = profileRepository.getByChatId(chatId).orElseThrow(()->new CustomerNotFoundException(chatId));
        return isCorrect(profile,verificationCode);
    }

    @Override
    public Optional<Profile> getByChatId(String chatId) {
        return profileRepository.getByChatId(chatId);
    }

    private Boolean isCorrect(Profile profile,String verification){
        if(profile.getOtp().getVerificationCode().equals(verification)) {
            LocalDateTime localDateTime = LocalDateTime.now();
            Duration duration = Duration.between(profile.getOtp().getCreatedDate(), localDateTime);
            Long minute = duration.toMinutes();
            log.info("time difference: {}",minute);
            if (minute < 2L) {
                profile.setVerified(true);
                profileRepository.save(profile);
                return true;
            }
        }
        return false;
    }

    private OTP createOtp() {
        OTP otp = new OTP();
        otp.setVerificationCode(String.valueOf(new Random().nextInt(900000) + 100000));
        return otp;
    }

}
