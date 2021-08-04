package zw.co.cassavasmartech.ecocashchatbotcore.sms;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "sms.endpoint")
@Configuration
@Validated
@Getter
@Setter
public class SmsProperties {

    @NotBlank
    private String sender;

    @NotBlank
    private String smsQueueName;

    @NotBlank
    private String endPointUrl;

    @NotBlank
    private String smsDispatchPeriodCron;

    @NotBlank
    private String user;

    @NotBlank
    private String password;

}
