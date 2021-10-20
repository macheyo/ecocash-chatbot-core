package zw.co.cassavasmartech.ecocashchatbotcore.infobip;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "ecocash.infobip.adapter.config")
@Getter
@Setter
@Validated
public class InfoBipAdapterConfigurationProperties {
    @NotNull
    private String url;

}
