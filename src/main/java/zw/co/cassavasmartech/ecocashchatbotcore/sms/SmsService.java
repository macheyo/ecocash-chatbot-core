package zw.co.cassavasmartech.ecocashchatbotcore.sms;

public interface SmsService {
    public void sendSms(String msisdn, String verificationCode);
}
