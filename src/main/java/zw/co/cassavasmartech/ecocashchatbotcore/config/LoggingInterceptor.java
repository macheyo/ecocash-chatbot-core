package zw.co.cassavasmartech.ecocashchatbotcore.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    final static Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        traceRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        traceResponse(response);
        return response;
    }

    private void traceRequest(HttpRequest request, byte[] body) throws IOException {
        logger.info("request begin");
        logger.info("URI         : {}", request.getURI());
        logger.info("Method      : {}", request.getMethod());
        logger.info("Headers     : {}", request.getHeaders());
        logger.info("Request body: {}", new String(body, "UTF-8"));
        logger.info("Request End");
    }

    private void traceResponse(ClientHttpResponse response) throws IOException {
        StringBuilder inputStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"));
        String line = bufferedReader.readLine();
        while (line != null) {
            inputStringBuilder.append(line);
            inputStringBuilder.append('\n');
            line = bufferedReader.readLine();
        }
        final HttpHeaders headers = response.getHeaders();

        logger.info("Response Begin");
        logger.info("Status code  : {}", response.getStatusCode());
        logger.info("Status text  : {}", response.getStatusText());
        logger.info("Headers      : {}", headers);
        logger.info("Response body: {}", inputStringBuilder.toString());
        logger.info("Response end");

        if (headers.containsKey("Content-Type")) {
            final String contentTypes = headers
                    .get("Content-Type")
                    .stream()
                    .collect(Collectors.joining(","));
            if (contentTypes.toLowerCase().contains("text/html") && inputStringBuilder.toString().startsWith("<")) {
                headers.setContentType(MediaType.APPLICATION_XML);
                logger.info("Headers      : {}", headers);
            }
        }
    }
}
