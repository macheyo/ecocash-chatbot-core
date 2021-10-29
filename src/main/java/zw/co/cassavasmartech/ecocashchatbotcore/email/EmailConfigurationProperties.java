package zw.co.cassavasmartech.ecocashchatbotcore.email;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "spring.mail")
public class EmailConfigurationProperties {
    private String host;
    private String auth;
    private String startTsl;
    private String defaultEncoding;
    private String port;
    private String username;
    private String password;
}
