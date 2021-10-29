package zw.co.cassavasmartech.ecocashchatbotcore.telegram;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "ecocash.chatbot.core.telegram.api.config")
@Getter
@Setter
@Validated
public class TelegramConfigurationProperties {
    @NotNull
    private String sendMessageUrl;
    @NotNull
    private String sendDocumentUrl;
    @NotNull
    private String token;

}
