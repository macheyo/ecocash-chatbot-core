package zw.co.cassavasmartech.ecocashchatbotcore.selfServiceCore;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.common.MobileNumberFormater;
import zw.co.cassavasmartech.ecocashchatbotcore.config.SelfServiceConfigurationProperties;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Answer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.AnswerStatus;
import zw.co.cassavasmartech.ecocashchatbotcore.model.SubscriberDto;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;
import java.util.Objects;

import static zw.co.cassavasmartech.ecocashchatbotcore.common.Util.buildJsonHttpHeaders;

@Component
@Slf4j
public class SelfServiceCoreInvokerImpl implements SelfServiceCoreInvoker {
    @Autowired
    MobileNumberFormater mobileNumberFormater;
    @Autowired
    SelfServiceConfigurationProperties selfServiceConfigurationProperties;
    @Autowired
    RestTemplate restTemplate;

    @Override
    public Boolean invokeIsEnrolled(String msisdn) {
        String minimumMsisdn = mobileNumberFormater.formatMsisdnMinimum(msisdn);
        log.debug("Processing customer enrollment lookup transaction request for {} to url {}", msisdn, selfServiceConfigurationProperties.getSelfServiceEndPointUrl());
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(selfServiceConfigurationProperties.getSelfServiceEndPointUrl()+"/subscriber/isEnrolled/"+minimumMsisdn);
        final HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        final HttpEntity<String> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,requestEntity, String.class);
        return Boolean.valueOf(responseEntity.getBody());
    }

    @Override
    public List<Answer> InvokeGetAnswerByMsisdnAndAnswerStatus(String msisdn) {
        String minimumMsisdn = mobileNumberFormater.formatMsisdnMinimum(msisdn);
        log.debug("Processing customer answer lookup transaction request for {} to url {}", msisdn, selfServiceConfigurationProperties.getSelfServiceEndPointUrl());
        final URI uri = UriComponentsBuilder.fromHttpUrl(selfServiceConfigurationProperties.getSelfServiceEndPointUrl()+"/answer/findAnswersByMsisdnAndAnswerStatus/"+minimumMsisdn+"/"+ AnswerStatus.ACTIVE).buildAndExpand().toUri();
        final RequestEntity<?> entity = new RequestEntity<>(buildJsonHttpHeaders(), HttpMethod.GET, uri);
        final ApiResponse response = restTemplate.exchange(entity, ApiResponse.class).getBody();

        log.info("The Response {}", response.getBody());
        return (List<Answer>) response.getBody();
    }

    @Override
    public SubscriberDto InvokeGetAlternative(String msisdn) {
        String minimumMsisdn = mobileNumberFormater.formatMsisdnMinimum(msisdn);
        log.debug("Processing customer answer lookup transaction request for {} to url {}", msisdn, selfServiceConfigurationProperties.getSelfServiceEndPointUrl());
        final URI uri = UriComponentsBuilder.fromHttpUrl(selfServiceConfigurationProperties.getSelfServiceEndPointUrl()+"/subscriber/"+minimumMsisdn).buildAndExpand().toUri();
        final RequestEntity<?> entity = new RequestEntity<>(buildJsonHttpHeaders(), HttpMethod.GET, uri);
        final ApiResponse<SubscriberDto> response = restTemplate.exchange(entity, ApiResponse.class).getBody();
        ObjectMapper mapper = new ObjectMapper();
        SubscriberDto subscriberDto;
        subscriberDto = mapper.convertValue(Objects.requireNonNull(response).getBody(), SubscriberDto.class);
        log.info("The Response {}", subscriberDto);
        return subscriberDto;
    }

}

