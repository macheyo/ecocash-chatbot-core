package zw.co.cassavasmartech.ecocashchatbotcore.service;

import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Customer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Profile;

import java.util.List;
import java.util.Optional;

@Service
public interface ProfileService {
    Profile save(String msisdn, Profile profile);
    List<EntityModel<Profile>> findAll();
    Boolean generateOtp(String chatId);

    Boolean verifyCustomer(String id, String verificationCode);
}
