package zw.co.cassavasmartech.ecocashchatbotcore.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

@Slf4j
public class ResponseHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) {

        return true;
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) {

    }

}
