package zw.co.cassavasmartech.ecocashchatbotcore.tariffsService;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Configuration
@ConfigurationProperties(prefix = "ecocash.chatbot.core.tariff.service.config")
@Getter
@Setter
@Validated
public class TariffServiceConfigurationProperties {
    @NotEmpty
    private String sendMoneyTariffRegisteredEndPointUrl;
    @NotEmpty
    private String sendMoneyTariffUnregisteredEndPointUrl;
    @NotEmpty
    private String billerTariffUnEndPointUrl;
    @NotEmpty
    private String merchantTariffEndPointUrl;
}
