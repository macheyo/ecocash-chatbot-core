package zw.co.cassavasmartech.ecocashchatbotcore.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RestConfiguration {

    private final HttpClientConfig httpClientConfig;
    @Value("${sms.endpoint.user}")
    private String smsApiUser;
    @Value("${sms.endpoint.password}")
    private String smsPassword;
    public static final String SMS_REST_TEMPLATE = "SMS_REST_TEMPLATE";
    public static final String SMS_REST_TEMPLATE_V2 = "SMS_REST_TEMPLATE_V2";
    public static final String ECOCASH_REST_TEMPLATE = "ECOCASH_REST_TEMPLATE";

    @Bean(name = SMS_REST_TEMPLATE)
    public RestTemplate smsRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .basicAuthentication(smsApiUser, smsPassword)
                .setConnectTimeout(httpClientConfig.connectTimeout)
                .setReadTimeout(httpClientConfig.getReadTimeout())
                .build();
    }

    @Bean(name = SMS_REST_TEMPLATE_V2)
    public RestTemplate smsRestTemplateV2(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(httpClientConfig.connectTimeout)
                .setReadTimeout(httpClientConfig.getReadTimeout())
                .build();
    }

    @Bean
    @Primary
    public RestTemplate restTemplate() {
        final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5 * 1000);
        requestFactory.setReadTimeout(18 * 1000);
        return new RestTemplate(requestFactory);
    }

    @Bean(name = ECOCASH_REST_TEMPLATE)
    public RestTemplate restTemplateEcocash(@Qualifier("ecocashAggregatorRequestInterceptor")
                                                    LoggingInterceptor loggingRequestInterceptor) {
        return getRestTemplateWithLoggingInterceptor(loggingRequestInterceptor);
    }

    private RestTemplate getRestTemplateWithLoggingInterceptor(@Qualifier("ecocashAggregatorRequestInterceptor") LoggingInterceptor loggingRequestInterceptor) {
        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(loggingRequestInterceptor);
        restTemplate.setInterceptors(interceptors);

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        List<MediaType> mediaTypes = Arrays.asList(MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML,MediaType.TEXT_HTML);
        converter.setSupportedMediaTypes(mediaTypes);
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }

    @Bean(name = "ecocashAggregatorRequestInterceptor")
    public LoggingInterceptor loggingRequestInterceptor() {
        return new LoggingInterceptor();
    }


}
