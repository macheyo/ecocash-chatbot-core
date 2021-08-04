package zw.co.cassavasmartech.ecocashchatbotcore.notification;


import zw.co.cassavasmartech.ecocashchatbotcore.sms.Sms;
import zw.co.cassavasmartech.ecocashchatbotcore.sms.SmsDispatchStrategy;

public interface NotificationService {

    void sendSms(Sms sms, SmsDispatchStrategy dispatchStrategy);

    void sendSms(String to, SmsDispatchStrategy dispatchStrategy, String message);

}
