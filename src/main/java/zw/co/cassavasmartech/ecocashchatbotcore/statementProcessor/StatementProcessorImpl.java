package zw.co.cassavasmartech.ecocashchatbotcore.statementProcessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
import java.util.List;

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

    @Override
    public Statement getStatement(StatementRequest statementRequest) {
        String token = tokenService.getToken(statementServiceConfigurationProperties.getUsername(),
                statementServiceConfigurationProperties.getPassword());
        httpSession.setAttribute("TOKEN", token);
        return coreInvoker.invoke(statementRequest,
                statementServiceConfigurationProperties.getStatementServiceEndPointUrl() + "/customer/statement/request",
                HttpMethod.POST,
                new ParameterizedTypeReference<ApiResponse<Statement>>() {});
    }
}
