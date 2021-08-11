package zw.co.cassavasmartech.ecocashchatbotcore.invoker;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiHeader;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ResponseHandler;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.BusinessException;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.ConnectionException;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@Slf4j
@RequiredArgsConstructor
public class CoreInvokerImpl implements CoreInvoker {
    private final RestTemplate restTemplate;
    private final ApiHeader apiHeader;


    @Override
    public <T, R> R invoke(T t, String resourceUrl, HttpMethod httpMethod, ParameterizedTypeReference parameterizedTypeReference) {
        try {

            RequestEntity<T> requestEntity = null;
            if(t == null) {
                requestEntity = new RequestEntity<>(apiHeader.headerWithToken(),
                        httpMethod, new URI(resourceUrl));
            }else{
                requestEntity = new RequestEntity<>(t, apiHeader.headerWithToken(),
                        httpMethod, new URI(resourceUrl));
            }

            restTemplate.setErrorHandler(new ResponseHandler());
            final ResponseEntity<ApiResponse<R>> responseEntity
                    = restTemplate.exchange(requestEntity, parameterizedTypeReference);

            final ApiResponse<R> apiResponse = responseEntity.getBody();

            if(apiResponse.getStatus() == HttpStatus.OK.value()){
                return apiResponse.getBody();
            }
            throw new BusinessException(apiResponse.getMessage());
        } catch (URISyntaxException e) {
            throw new ConnectionException(e);
        }
    }

}
