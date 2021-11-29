package zw.co.cassavasmartech.ecocashchatbotcore.ussd;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "ecocash.ussd.push.config")
@Getter
@Setter
@Validated
public class UssdPushServiceConfigurationProperties {
    @NotNull
    private String url;
}
