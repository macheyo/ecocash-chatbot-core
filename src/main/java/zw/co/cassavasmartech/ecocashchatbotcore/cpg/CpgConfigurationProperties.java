package zw.co.cassavasmartech.ecocashchatbotcore.cpg;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Configuration
@ConfigurationProperties(prefix = "ecocash.chatbot.core.cpg-api.config")
@Getter
@Setter
@Validated
public class CpgConfigurationProperties {
    @NotEmpty
    private String keystoreType;
    @NotEmpty
    private String keystoreLocation;
    @NotEmpty
    private String keystorePassword;
    @NotEmpty
    private String keystoreAlias;
    @NotEmpty
    private String cpgEndPointUrl;
    @NotEmpty
    private String cpgEndPointUrl2;
    @NotEmpty
    private String cpgEndPointUrl3;
    @NotEmpty
    private String cpgCallBackUrl;
    @NotEmpty
    private String subscriberToMerchantTranType;
    @NotEmpty
    private String merchantToSubscriberTranType;
    @NotEmpty
    private String merchantToMerchantTranType;
    @NotEmpty
    private String pinResetTranType;
    @NotEmpty
    private String customerLookupTranType;
    @NotEmpty
    private String customerStatementTranType;
    @NotEmpty
    private String subscriberToSubscriberTranType;
    @NotEmpty
    private String subscriberToBillerTranType;
    @NotEmpty
    private String billerLookupTranType;
    @NotEmpty
    private String subscriberAirtimeTranType;
    @NotEmpty
    private String registrationTrasType;
    @NotEmpty
    private String merchantLookupTranType;
    @NotEmpty
    private String customerValidationTranType;
    @NotEmpty
    private String currency;


}
