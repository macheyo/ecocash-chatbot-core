package zw.co.cassavasmartech.ecocashchatbotcore.sms;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.common.MessagePropertiesService;
import zw.co.cassavasmartech.ecocashchatbotcore.common.MobileNumberFormater;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.EipTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.invoker.CoreInvoker;
import zw.co.cassavasmartech.ecocashchatbotcore.selfServiceCore.data.ReversalDto;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static zw.co.cassavasmartech.ecocashchatbotcore.common.Util.buildEIPJsonHttpHeaders;

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
    public void sendSms(String msisdn, String verificationCode) {
        final String notificationMessage = String.format(messagePropertiesService.getByKey("messages.otp"), verificationCode);
        final URI uri = UriComponentsBuilder.fromHttpUrl(smsProperties.getEndPointUrl()).buildAndExpand().toUri();
        final RequestEntity<Sms> requestEntity = new RequestEntity<>(Sms.builder().to(mobileNumberFormater.formatMobileNumberInternational(msisdn)).text(notificationMessage).build(), buildEIPJsonHttpHeaders(), HttpMethod.POST, uri);
        final ResponseEntity<Sms> responseEntity = restTemplate.exchange(requestEntity, Sms.class);
        final Sms response = responseEntity.getBody();
        log.info("Sms notification response {}", response);
    }
}
