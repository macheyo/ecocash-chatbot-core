package zw.co.cassavasmartech.ecocashchatbotcore.sms;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static zw.co.cassavasmartech.ecocashchatbotcore.config.RestConfiguration.SMS_REST_TEMPLATE;

@Slf4j
@Component
@RabbitListener(queues = {"${sms.endpoint.smsQueueName}"}, concurrency = "${sms.notification-queue.concurrency}")
public class SmsNotificationReceiverImpl implements SmsNotificationReceiver {

    private final RestTemplate restTemplate;
    private final SmsProperties smsProperties;

    public SmsNotificationReceiverImpl(@Qualifier(value = SMS_REST_TEMPLATE) RestTemplate restTemplate, SmsProperties smsProperties) {
        this.restTemplate = restTemplate;
        this.smsProperties = smsProperties;
    }

    @Override
    @RabbitHandler
    public void receive(@Payload Sms sms) {

        log.debug("Processing sms request {}", sms);
        try {
            log.info("Dispatching sms notification:  {}", sms);
            String response = restTemplate.postForObject(smsProperties.getEndPointUrl(), sms, String.class);
            log.info("Sms dispatch response: {}", response);
        } catch (Exception ex) {
            log.error("Failed to send sms =>", ex);
        }
    }
}
