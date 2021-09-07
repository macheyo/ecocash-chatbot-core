package zw.co.cassavasmartech.ecocashchatbotcore.twilio;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwilioServiceImpl implements TwilioService{
    @Autowired
    TwilioConfigurationProperties twilioConfigurationProperties;
    @Override
    public Message sendMessage(String chatId, String text) {
        Twilio.init(twilioConfigurationProperties.getSid(),twilioConfigurationProperties.getToken());
        return Message.creator(new PhoneNumber(chatId),new PhoneNumber(twilioConfigurationProperties.getNumber()),text).create();
    }
}
