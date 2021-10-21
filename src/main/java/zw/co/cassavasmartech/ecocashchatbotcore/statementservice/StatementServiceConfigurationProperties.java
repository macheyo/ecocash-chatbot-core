package zw.co.cassavasmartech.ecocashchatbotcore.statementservice;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Configuration
@ConfigurationProperties(prefix = "ecocash.chatbot.core.statement.service.config")
@Getter
@Setter
@Validated
public class StatementServiceConfigurationProperties {
    @NotEmpty
    private String statementServiceEndPointUrl;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @NotEmpty
    private String ngrokServiceEndpointUrl;

}
