package zw.co.cassavasmartech.ecocashchatbotcore.twilio;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "ecocash.chatbot.core.twilio.api.config")
@Getter
@Setter
@Validated
public class TwilioConfigurationProperties {
    @NotNull
    private String sid;
    @NotNull
    private String token;
    @NotNull
    private String number;
}
