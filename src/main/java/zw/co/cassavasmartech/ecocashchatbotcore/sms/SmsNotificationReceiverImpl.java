package zw.co.cassavasmartech.ecocashchatbotcore.sms;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static zw.co.cassavasmartech.ecocashchatbotcore.config.RestConfiguration.SMS_REST_TEMPLATE_V2;

@Slf4j
@Component
@RabbitListener(queues = {"${sms.endpoint.smsQueueName}"}, concurrency = "${sms.notification-queue.concurrency}")
public class SmsNotificationReceiverImpl implements SmsNotificationReceiver {

    private final RestTemplate restTemplate;
    private final SmsProperties smsProperties;

    @Value("${sms.basic.auth.v2}")
    private static String basicAuth;
    @Value("${sms.url.v2}")
    private String smsUrlV2;

    public SmsNotificationReceiverImpl(@Qualifier(value = SMS_REST_TEMPLATE_V2) RestTemplate restTemplate, SmsProperties smsProperties) {
        this.restTemplate = restTemplate;
        this.smsProperties = smsProperties;
    }

    @Override
    @RabbitHandler
    public void receive(@Payload Sms sms) {

        log.debug("Processing sms request {}", sms);
        try {
            final URI uri = new URI(smsUrlV2);
            final RequestEntity<Sms> requestEntity = new RequestEntity<>(sms, buildJsonHttpHeaders(), HttpMethod.POST, uri);
            final ResponseEntity<String> responseEntity = new RestTemplate().exchange(requestEntity, String.class);
            log.info("Sms dispatch response: {}", responseEntity.getBody());
        } catch (Exception ex) {
            log.error("Failed to send sms =>", ex);
        }
    }

    public static HttpHeaders buildJsonHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, basicAuth);
        return headers;
    }

}
