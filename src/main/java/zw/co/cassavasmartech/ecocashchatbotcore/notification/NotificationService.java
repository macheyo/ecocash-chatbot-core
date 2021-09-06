package zw.co.cassavasmartech.ecocashchatbotcore.notification;


import zw.co.cassavasmartech.ecocashchatbotcore.sms.Sms;

public interface NotificationService {

    void sendSms(Sms sms);

}
