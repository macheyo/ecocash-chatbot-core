package zw.co.cassavasmartech.ecocashchatbotcore.statementProcessor;

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
import zw.co.cassavasmartech.ecocashchatbotcore.config.SelfServiceConfigurationProperties;
import zw.co.cassavasmartech.ecocashchatbotcore.config.StatementServiceConfigurationProperties;
import zw.co.cassavasmartech.ecocashchatbotcore.invoker.CoreInvoker;
import zw.co.cassavasmartech.ecocashchatbotcore.model.*;
import zw.co.cassavasmartech.ecocashchatbotcore.selfServiceCore.SelfServiceCoreProcessor;
import zw.co.cassavasmartech.ecocashchatbotcore.token.TokenService;

import javax.servlet.http.HttpSession;
import java.net.URI;
import java.util.List;

import static zw.co.cassavasmartech.ecocashchatbotcore.common.Util.buildJsonHttpHeaders;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatementProcessorImpl implements StatementProcessor {
    @Autowired
    MobileNumberFormater mobileNumberFormater;
    @Autowired
    StatementServiceConfigurationProperties statementServiceConfigurationProperties;
    @Autowired
    private HttpSession httpSession;

    private final CoreInvoker coreInvoker;
    private final TokenService tokenService;
    private final RestTemplate restTemplate;

    @Override
    public Statement getStatement(StatementRequest statementRequest) {

        String token = tokenService.getToken(statementServiceConfigurationProperties.getUsername(),
                statementServiceConfigurationProperties.getPassword());
        httpSession.setAttribute("TOKEN", token);
        return coreInvoker.invoke(statementRequest,
                statementServiceConfigurationProperties.getStatementServiceEndPointUrl() + "/customer/statement/request/",
                HttpMethod.POST,
                new ParameterizedTypeReference<ApiResponse<Statement>>() {});
    }

    HttpHeaders buildJsonHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJNdW55YXJhZHppLlRha2F5aW5kaSIsImlhdCI6MTYyODc1Mzg0MywiZXhwIjoxNjI4Nzc5NzYzfQ.ameDqFtP39B7PvzqerQ5yrtVFOQgxn43nFWneHg3brJWeu6ggzvNmy9nkYQL6vrDawKamDMEZCSQA4Fz0agm9A");
        return headers;
    }
}
