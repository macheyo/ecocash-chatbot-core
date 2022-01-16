package zw.co.cassavasmartech.ecocashchatbotcore.sms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import zw.co.cassavasmartech.ecocashchatbotcore.common.MessagePropertiesService;
import zw.co.cassavasmartech.ecocashchatbotcore.common.MobileNumberFormater;
import zw.co.cassavasmartech.ecocashchatbotcore.invoker.CoreInvoker;

import java.net.URI;

import static zw.co.cassavasmartech.ecocashchatbotcore.common.Util.buildEIPJsonHttpHeaders;
import static zw.co.cassavasmartech.ecocashchatbotcore.common.Util.buildSMSJsonHttpHeaders;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService{
    @Autowired
    MobileNumberFormater mobileNumberFormater;

    @Autowired
    CoreInvoker coreInvoker;

    @Autowired
    SmsProperties smsProperties;

    @Autowired
    MessagePropertiesService messagePropertiesService;

    @Autowired
    RestTemplate restTemplate;

    @Async("threadPoolTaskExecutor")
    @Override
    public void sendSms(String msisdn, String verificationCode, String messageKey) {
        final String notificationMessage = String.format(messagePropertiesService.getByKey(messageKey), verificationCode);
        final URI uri = UriComponentsBuilder.fromHttpUrl(smsProperties.getEndPointUrl()).buildAndExpand().toUri();
        final RequestEntity<Sms> requestEntity = new RequestEntity<>(Sms.builder().to(mobileNumberFormater.formatMobileNumberInternational(msisdn)).text(notificationMessage).build(), buildSMSJsonHttpHeaders(), HttpMethod.POST, uri);
        final ResponseEntity<Sms> responseEntity = restTemplate.exchange(requestEntity, Sms.class);
        final Sms response = responseEntity.getBody();
        log.info("Sms notification response {}", response);
    }
}
