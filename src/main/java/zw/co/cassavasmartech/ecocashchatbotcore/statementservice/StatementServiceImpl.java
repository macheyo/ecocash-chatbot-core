package zw.co.cassavasmartech.ecocashchatbotcore.statementservice;

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
import zw.co.cassavasmartech.ecocashchatbotcore.common.PassThroughUtil;
import zw.co.cassavasmartech.ecocashchatbotcore.invoker.CoreInvoker;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Statement;
import zw.co.cassavasmartech.ecocashchatbotcore.model.StatementRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.token.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatementServiceImpl implements StatementService {
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
    public ResponseEntity<byte[]> getStatementFile(String documentId, HttpServletRequest req, HttpServletResponse resp) {
//        passThroughUtil.forwardRequest(String.format("%s/customer/statement/downloadFile/%s", statementServiceConfigurationProperties.getStatementServiceEndPointUrl(), documentId),
//                "GET", req, resp);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_PDF, MediaType.APPLICATION_OCTET_STREAM));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        final URI uri = UriComponentsBuilder.fromHttpUrl(String.format("%s/customer/statement/downloadFile/%s", statementServiceConfigurationProperties.getStatementServiceEndPointUrl(), documentId)).buildAndExpand().toUri();
        return restTemplate.exchange(uri, HttpMethod.GET, entity, byte[].class);
    }


    HttpHeaders buildJsonHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJNdW55YXJhZHppLlRha2F5aW5kaSIsImlhdCI6MTYyODc1Mzg0MywiZXhwIjoxNjI4Nzc5NzYzfQ.ameDqFtP39B7PvzqerQ5yrtVFOQgxn43nFWneHg3brJWeu6ggzvNmy9nkYQL6vrDawKamDMEZCSQA4Fz0agm9A");
        return headers;
    }
}
