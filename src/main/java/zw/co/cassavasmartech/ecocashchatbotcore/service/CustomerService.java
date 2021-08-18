package zw.co.cassavasmartech.ecocashchatbotcore.service;

import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.data.*;
import zw.co.cassavasmartech.ecocashchatbotcore.model.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Service
public interface CustomerService {
    Customer save(Customer customer);
    List<EntityModel<Customer>> findAll();
    Customer getByChatId(String chatId);
    EnrollmentResponse isEnrolled(String id);
    Optional<Customer> findById(Long id);
    List<Answer> getAnswers(String chatId);
    SubscriberDto getAlternative(String chatId);
    Boolean pinReset(String id);

    Statement getStatement(String id, StatementRequest statementRequest) throws ParseException;

    TransactionResponse billerLookup(BillerLookupRequest billerLookupRequest);

    TransactionResponse payBiller(String chatId, SubscriberToBillerRequest subscriberToBillerRequest);

    TransactionResponse payMerchant(String chatId, SubscriberToMerchantRequest subscriberToMerchantRequest);

    TransactionResponse buyAirtime(String chatId, SubscriberAirtimeRequest subscriberAirtimeRequest);

    void getStatementFile(String documentId, HttpServletRequest req, HttpServletResponse resp);

    Boolean verifyAnswers(String chatId, VerifyAnswerRequest verifyAnswerRequest);

    TransactionResponse customerLookup(SubscriberDto subscriberDto);

    TransactionResponse sendMoney(String chatId, SubscriberToSubscriberRequest subscriberToSubscriberRequest);

    Boolean verifyAnswer(String chatId, VerifyAnswerRequest verifyAnswerRequest);

    TransactionResponse registerCustomer(Registration registration);
}
