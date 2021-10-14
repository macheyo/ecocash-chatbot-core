package zw.co.cassavasmartech.ecocashchatbotcore.payment.cpg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import zw.co.cassavasmartech.esb.commons.data.PostTransaction;
import zw.co.cassavasmartech.esb.commons.data.PostTransactionResponse;
import zw.co.cassavasmartech.esb.exception.BusinessRuntimeException;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class PaymentGatewayEndpointInvokerImpl implements PaymentGatewayEndpointInvoker {

    private static final Logger logger = LoggerFactory.getLogger(PaymentGatewayEndpointInvokerImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public PostTransactionResponse invoke(PostTransaction request, String endpointUrl) throws BusinessRuntimeException {
        try {
            logger.info("Invoking epg with request: {},\nend point url: {}", request, endpointUrl);
            final HttpHeaders headers = buildHttpHeaders();
            final RequestEntity<PostTransaction> requestEntity = new RequestEntity<>(request, headers, HttpMethod.POST,
                    new URI(endpointUrl));
            final ResponseEntity<PostTransactionResponse> responseEntity = restTemplate.exchange(requestEntity, PostTransactionResponse.class);
            logger.info("Epg Response : {}", responseEntity.getBody());
            return responseEntity.getBody();
        } catch (URISyntaxException use) {
            throw new BusinessRuntimeException(use);
        }
    }

    private HttpHeaders buildHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

}
