package zw.co.cassavasmartech.ecocashchatbotcore.token;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ApiResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.statementProcessor.StatementServiceConfigurationProperties;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.BusinessException;
import zw.co.cassavasmartech.ecocashchatbotcore.invoker.CoreInvoker;
import zw.co.cassavasmartech.ecocashchatbotcore.token.data.TokenRequest;


@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {

    @Autowired
    StatementServiceConfigurationProperties statementServiceConfigurationProperties;
    private final CoreInvoker coreInvoker;

    @Override
    public String getToken(String username,String password){
        try {
            TokenRequest tokenRequest = new TokenRequest(username, password);
            return coreInvoker.invoke(tokenRequest,
                    statementServiceConfigurationProperties.getStatementServiceEndPointUrl()+"/api/auth/generatetoken",
                    HttpMethod.POST,
                    new ParameterizedTypeReference<ApiResponse<String>>() {});
        }catch(Exception e){
            throw new BusinessException(e.getMessage());
        }

    }

}
