package zw.co.cassavasmartech.ecocashchatbotcore.selfservice;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Configuration
@ConfigurationProperties(prefix = "ecocash.chatbot.core.self.service.config")
@Getter
@Setter
@Validated
public class SelfServiceConfigurationProperties {
    @NotEmpty
    private String selfServiceEndPointUrl;

}
