package zw.co.cassavasmartech.ecocashchatbotcore.selfservice;

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
import zw.co.cassavasmartech.ecocashchatbotcore.selfservice.data.EcocashTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.selfservice.data.ReversalApproval;
import zw.co.cassavasmartech.ecocashchatbotcore.selfservice.data.ReversalDto;

import java.util.List;
import java.util.Optional;

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

    @Override
    public HttpEntity<ApiResponse<Optional<EcocashTransaction>>> validateReversal(String msisdn, String reference) {
        String minimumMsisdn = mobileNumberFormater.formatMsisdnMinimum(msisdn);
        log.debug("Processing validation of reversal {} to url {}", msisdn, selfServiceConfigurationProperties.getSelfServiceEndPointUrl());
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(selfServiceConfigurationProperties.getSelfServiceEndPointUrl()+ "/reversal/validate/"+minimumMsisdn+"/"+reference);
        final HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        final HttpEntity<ApiResponse<Optional<EcocashTransaction>>> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, requestEntity, new ParameterizedTypeReference<ApiResponse<Optional<EcocashTransaction>>>() {});
        return responseEntity;

    }

    @Override
    public HttpEntity<ApiResponse<Optional<ReversalDto>>> initiateReversal(String msisdn, String reference) {
        String minimumMsisdn = mobileNumberFormater.formatMsisdnMinimum(msisdn);
        log.debug("Processing initiation of reversal {} to url {}", msisdn, selfServiceConfigurationProperties.getSelfServiceEndPointUrl());
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(selfServiceConfigurationProperties.getSelfServiceEndPointUrl()+ "/reversal/initiate/"+minimumMsisdn+"/"+reference);
        final HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        final HttpEntity<ApiResponse<Optional<ReversalDto>>> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, requestEntity, new ParameterizedTypeReference<ApiResponse<Optional<ReversalDto>>>() {});
        return responseEntity;
    }

    @Override
    public HttpEntity<ApiResponse<Optional<ReversalDto>>> approveReversal(ReversalApproval reversalApproval) {
        log.debug("Processing approval of reversal {} to url {}", reversalApproval.getReversalId(), selfServiceConfigurationProperties.getSelfServiceEndPointUrl());
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(selfServiceConfigurationProperties.getSelfServiceEndPointUrl()+ "/reversal/approve");
        final HttpEntity<?> requestEntity = new HttpEntity<>(reversalApproval, headers);
        final HttpEntity<ApiResponse<Optional<ReversalDto>>> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, requestEntity, new ParameterizedTypeReference<ApiResponse<Optional<ReversalDto>>>() {});
        return responseEntity;
    }

    @Override
    public HttpEntity<ApiResponse<List<ReversalDto>>> pendingReversals(String msisdn) {
        String minimumMsisdn = mobileNumberFormater.formatMsisdnMinimum(msisdn);
        log.debug("Processing validation of reversal {} to url {}", msisdn, selfServiceConfigurationProperties.getSelfServiceEndPointUrl());
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(selfServiceConfigurationProperties.getSelfServiceEndPointUrl()+ "/reversal/recipientPending/"+minimumMsisdn);
        final HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        final HttpEntity<ApiResponse<List<ReversalDto>>> responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, requestEntity, new ParameterizedTypeReference<ApiResponse<List<ReversalDto>>>() {});
        return responseEntity;
    }
}
