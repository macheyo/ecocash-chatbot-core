package zw.co.cassavasmartech.ecocashchatbotcore.eip;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Configuration
@ConfigurationProperties(prefix = "ecocash.chatbot.core.eip.api.config")
@Getter
@Setter
@Validated
public class EipConfigurationProperties {
    @NotNull
    private String postUrl;
    @NotNull
    private String reverseUrl;
    @NotNull
    private String lookupUrl;
    @NotNull
    private String notifyUrl;
    @NotNull
    private String username;
    @NotNull
    private String password;
}
