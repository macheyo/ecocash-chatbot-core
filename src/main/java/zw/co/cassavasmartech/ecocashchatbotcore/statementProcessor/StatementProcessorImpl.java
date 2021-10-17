package zw.co.cassavasmartech.ecocashchatbotcore.statementProcessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.common.MobileNumberFormater;
import zw.co.cassavasmartech.ecocashchatbotcore.common.PassThroughUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.invoker.CoreInvoker;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Statement;
import zw.co.cassavasmartech.ecocashchatbotcore.model.StatementRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.token.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
    @Autowired
    private PassThroughUtil passThroughUtil;

    private final CoreInvoker coreInvoker;
    private final TokenService tokenService;

    @Override
    public Statement getStatement(StatementRequest statementRequest) {

        String token = tokenService.getToken(statementServiceConfigurationProperties.getUsername(),
                statementServiceConfigurationProperties.getPassword());
        httpSession.setAttribute("TOKEN", token);
        return coreInvoker.invoke(statementRequest,
                statementServiceConfigurationProperties.getStatementServiceEndPointUrl() + "/customer/statement/request/",
                HttpMethod.POST,
                new ParameterizedTypeReference<ApiResponse<Statement>>() {
                });
    }

    @Override
    public void getStatementFile(String documentId, HttpServletRequest req, HttpServletResponse resp) {
        passThroughUtil.forwardRequest(String.format("%s/customer/statement/downloadFile/%s", statementServiceConfigurationProperties.getStatementServiceEndPointUrl(), documentId),
                "GET", req, resp);
    }


    HttpHeaders buildJsonHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJNdW55YXJhZHppLlRha2F5aW5kaSIsImlhdCI6MTYyODc1Mzg0MywiZXhwIjoxNjI4Nzc5NzYzfQ.ameDqFtP39B7PvzqerQ5yrtVFOQgxn43nFWneHg3brJWeu6ggzvNmy9nkYQL6vrDawKamDMEZCSQA4Fz0agm9A");
        return headers;
    }
}
