package zw.co.cassavasmartech.ecocashchatbotcore.invoker;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

public interface CoreInvoker {

    <T, R> R invoke(T t, String resourceUrl, HttpMethod httpMethod, ParameterizedTypeReference parameterizedTypeReference);

}
