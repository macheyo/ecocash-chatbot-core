package zw.co.cassavasmartech.ecocashchatbotcore.sms.service;

import zw.co.cassavasmartech.ecocashchatbotcore.sms.Sms;

public interface SmsSender {

    void sendSms(Sms sms);

    void buildAndSend(String msisdn, String message);

    void buildAndSend(String sender,String msisdn, String message);

}
