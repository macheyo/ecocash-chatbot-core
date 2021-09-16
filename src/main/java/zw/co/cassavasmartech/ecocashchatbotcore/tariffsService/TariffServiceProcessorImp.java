package zw.co.cassavasmartech.ecocashchatbotcore.tariffsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import zw.co.cassavasmartech.ecocashchatbotcore.common.MobileNumberFormater;
import zw.co.cassavasmartech.ecocashchatbotcore.invoker.CoreInvoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import zw.co.cassavasmartech.ecocashchatbotcore.common.MobileNumberFormater;
import zw.co.cassavasmartech.ecocashchatbotcore.invoker.CoreInvoker;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PostTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PostTransactionResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionResponse;

import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Optional;

import static zw.co.cassavasmartech.ecocashchatbotcore.common.Util.buildJsonHttpHeaders;

@Component
@RequiredArgsConstructor
@Slf4j
public class TariffServiceProcessorImp implements TariffServiceProcessor {

    @Autowired
    TariffServiceConfigurationProperties tariffServiceConfigurationProperties;

    @Autowired
    RestTemplate restTemplate;


    private TransactionResponse invoke(String urlEndPoint, BigDecimal amount) {
        log.debug("Processing tariff transaction request: {​} on {}​​", amount, urlEndPoint);
        final URI uri = UriComponentsBuilder.fromHttpUrl(urlEndPoint+"/"+amount).buildAndExpand().toUri();
        final RequestEntity<?> requestEntity = new RequestEntity<>(null, buildJsonHttpHeaders(), HttpMethod.GET, uri);
        final ResponseEntity<TransactionResponse> responseEntity = restTemplate.exchange(requestEntity, TransactionResponse.class);
        final TransactionResponse response = responseEntity.getBody();
        log.info("Tariff transaction response {​}​", response);
        return response;
    }

    @Override
    public TransactionResponse tariffRegistered(BigDecimal amount) {
        return invoke(tariffServiceConfigurationProperties.getSendMoneyTariffRegisteredEndPointUrl(), amount);
    }
}
