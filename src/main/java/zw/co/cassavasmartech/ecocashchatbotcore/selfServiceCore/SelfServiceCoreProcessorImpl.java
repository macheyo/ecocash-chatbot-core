package zw.co.cassavasmartech.ecocashchatbotcore.selfServiceCore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.common.MobileNumberFormater;
import zw.co.cassavasmartech.ecocashchatbotcore.invoker.CoreInvoker;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Answer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.AnswerStatus;
import zw.co.cassavasmartech.ecocashchatbotcore.model.EnrollmentResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.model.SubscriberDto;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SelfServiceCoreProcessorImpl implements SelfServiceCoreProcessor{
    @Autowired
    MobileNumberFormater mobileNumberFormater;
    @Autowired
    SelfServiceConfigurationProperties selfServiceConfigurationProperties;
    @Autowired
    RestTemplate restTemplate;
    private final CoreInvoker coreInvoker;

    @Override
    public EnrollmentResponse isEnrolled(String msisdn) {
        String minimumMsisdn = mobileNumberFormater.formatMsisdnMinimum(msisdn);
        log.debug("Processing customer enrollment lookup transaction request for {} to url {}", msisdn, selfServiceConfigurationProperties.getSelfServiceEndPointUrl());
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(selfServiceConfigurationProperties.getSelfServiceEndPointUrl()+"/subscriber/isEnrolled/"+minimumMsisdn);
        final HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        final HttpEntity<String> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,requestEntity, String.class);
        return new EnrollmentResponse(responseEntity.getBody());

    }

    @Override
    public List<Answer> getAnswerByMsisdnAndAnswerStatus(String msisdn) {
        return coreInvoker.invoke(null,
                selfServiceConfigurationProperties.getSelfServiceEndPointUrl()+"/answer/findAnswersByMsisdnAndAnswerStatus/"+mobileNumberFormater.formatMsisdnMinimum(msisdn)+"/"+ AnswerStatus.ACTIVE,
                HttpMethod.GET,
                new ParameterizedTypeReference<ApiResponse<List<Answer>>>() {});
    }

    @Override
    public SubscriberDto getAlternative(String msisdn) {
        return coreInvoker.invoke(null,
                selfServiceConfigurationProperties.getSelfServiceEndPointUrl() + "/subscriber/" + mobileNumberFormater.formatMsisdnMinimum(msisdn),
                HttpMethod.GET,
                new ParameterizedTypeReference<ApiResponse<SubscriberDto>>() {});
    }
}
