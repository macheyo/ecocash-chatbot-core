package zw.co.cassavasmartech.ecocashchatbotcore.cpg;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PostTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PostTransactionResponse;

import java.net.URI;
import java.util.Optional;

import static zw.co.cassavasmartech.ecocashchatbotcore.common.Util.buildJsonHttpHeaders;

@Component
@Slf4j

public class PaymentGatewayInvokerImpl implements PaymentGatewayInvoker {

    @Autowired
    private  CpgConfigurationProperties configProperties;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Optional<PostTransactionResponse> invoke(PostTransaction postTransaction) {
        log.debug("Processing cpg transaction request {} to url {}", postTransaction, configProperties.getCpgEndPointUrl());
        final URI uri = UriComponentsBuilder.fromHttpUrl(configProperties.getCpgEndPointUrl()).buildAndExpand().toUri();
        final RequestEntity<PostTransaction> requestEntity = new RequestEntity<>(postTransaction, buildJsonHttpHeaders(), HttpMethod.POST, uri);
        final ResponseEntity<PostTransactionResponse> responseEntity = restTemplate.exchange(requestEntity, PostTransactionResponse.class);
        final PostTransactionResponse response = responseEntity.getBody();
        log.info("Cpg transaction response {}", response);
        return Optional.ofNullable(response);
    }
}
