package zw.co.cassavasmartech.ecocashchatbotcore.ussd;

import org.springframework.http.ResponseEntity;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PostTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.ussd.data.UssdPushRequest;

public interface UssdPushService {
    Boolean sendPrompt(UssdPushRequest ussdPushRequest);

    ResponseEntity<String> handleCallback(String input, String msisdn);
}
