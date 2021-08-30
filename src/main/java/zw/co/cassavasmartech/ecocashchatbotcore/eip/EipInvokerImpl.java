package zw.co.cassavasmartech.ecocashchatbotcore.eip;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.EipTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PostTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PostTransactionResponse;

import java.net.URI;
import java.util.Optional;

import static zw.co.cassavasmartech.ecocashchatbotcore.common.Util.buildEIPJsonHttpHeaders;
import static zw.co.cassavasmartech.ecocashchatbotcore.common.Util.buildJsonHttpHeaders;

@Component
@Slf4j
public class EipInvokerImpl implements EipInvoker{
    @Autowired
    EipConfigurationProperties eipConfigurationProperties;
    @Autowired
    RestTemplate restTemplate;

    @Override
    public Optional<EipTransaction> invoke(EipTransaction eipTransaction) {
        log.debug("Processing eip transaction request {} to url {}", eipTransaction, eipConfigurationProperties.getPostUrl());
        final URI uri = UriComponentsBuilder.fromHttpUrl(eipConfigurationProperties.getPostUrl()).buildAndExpand().toUri();
        final RequestEntity<EipTransaction> requestEntity = new RequestEntity<>(eipTransaction, buildEIPJsonHttpHeaders(), HttpMethod.POST, uri);
        final ResponseEntity<EipTransaction> responseEntity = restTemplate.exchange(requestEntity, EipTransaction.class);
        final EipTransaction response = responseEntity.getBody();
        log.info("EIP transaction response {}", response);
        return Optional.ofNullable(response);
    }
}
