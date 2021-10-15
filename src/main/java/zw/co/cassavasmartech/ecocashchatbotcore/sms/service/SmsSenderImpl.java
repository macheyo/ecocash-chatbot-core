package zw.co.cassavasmartech.ecocashchatbotcore.sms.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import zw.co.cassavasmartech.ecocashchatbotcore.common.MobileNumberFormater;
import zw.co.cassavasmartech.ecocashchatbotcore.config.SmsConfig;
import zw.co.cassavasmartech.ecocashchatbotcore.sms.Sms;

@Slf4j
@Service
public class SmsSenderImpl implements SmsSender {

    public SmsSenderImpl(@Qualifier(value = SmsConfig.SMS_REST_TEMPLATE) RestTemplate restTemplate, MobileNumberFormater mobileNumberFormater) {
        this.restTemplate = restTemplate;
        this.mobileNumberFormater = mobileNumberFormater;
    }

    private final RestTemplate restTemplate;
    private final MobileNumberFormater mobileNumberFormater;
    @Value("${sms.endpoint.url}")
    private String smsEndPointUrl;
    @Value("${sms.endpoint.sender}")
    private String senderName;

    @Override
    @Async
    public void sendSms(Sms sms) {
        try {
            log.trace("Dispatching smsNotification notification: {}, {}", sms, smsEndPointUrl);
            String response = restTemplate.postForObject(smsEndPointUrl, sms, String.class);
            log.trace("Sms dispatch response: {}", response);
        } catch (Exception ex) {
            log.error("Failed to send sms because :{} ", ex);
        }
    }

    @Override
    public void buildAndSend(String msisdn, String message) {
        final Sms sms = Sms.builder()
                .from(senderName)
                .text(message)
                .to(mobileNumberFormater.formatMobileNumberInternational(msisdn))
                .build();
        sendSms(sms);
    }

    @Override
    public void buildAndSend(String sender, String msisdn, String message) {
        final Sms sms = Sms.builder()
                .from(sender)
                .text(message)
                .to(mobileNumberFormater.formatMobileNumberInternational(msisdn))
                .build();
        sendSms(sms);
    }
}
