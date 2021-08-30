package zw.co.cassavasmartech.ecocashchatbotcore.twilio;

import com.twilio.rest.api.v2010.account.Message;
import org.springframework.stereotype.Service;

@Service
public interface TwilioService {
    Message sendMessage(String chatId, String message);
}
