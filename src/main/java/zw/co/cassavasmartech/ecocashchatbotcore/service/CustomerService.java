package zw.co.cassavasmartech.ecocashchatbotcore.service;

import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.data.SubscriberToBillerRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.data.SubscriberToMerchantRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.data.SubscriberToSubscriberRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.model.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Service
public interface CustomerService {
    Customer save(Customer customer);
    List<EntityModel<Customer>> findAll();
    Customer getByChatId(String chatId);
    Boolean isEnrolled(String id);
    Optional<Customer> findById(Long id);
    List<Answer> getAnswers(String chatId);
    SubscriberDto getAlternative(String chatId);
    Boolean pinReset(String id);
    Statement getStatement(String id, StatementRequest statementRequest) throws ParseException;
    TransactionResponse sendMoney(String chatId, SubscriberToSubscriberRequest subscriberToSubscriberRequest);

    TransactionResponse payMerchant(String chatId, SubscriberToMerchantRequest subscriberToMerchantRequest);

    TransactionResponse payBiller(String chatId, SubscriberToBillerRequest subscriberToBillerRequest);
}
