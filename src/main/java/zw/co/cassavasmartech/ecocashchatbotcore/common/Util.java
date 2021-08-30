package zw.co.cassavasmartech.ecocashchatbotcore.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.EipConfigurationProperties;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Util {

    public static HttpHeaders buildJsonHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.AUTHORIZATION, "Basic Y2Fzc2F2YWVjb25ldDolJEFwcmlsMTQxNA==");
        return headers;
    }
    public static String generateReference(String msisdn) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmssSSS");
        return new StringBuilder(msisdn).append(formatter.format(LocalDateTime.now())).toString();
    }

    public static HttpHeaders buildEIPJsonHttpHeaders(){
        HttpHeaders headers = new HttpHeaders() {
            {
                //String auth = eipConfigurationProperties.getUsername() + ":" + eipConfigurationProperties.getPassword();
                String auth = "developer:#pass";
                byte[] encodedAuth = Base64.getEncoder().encode(
                        auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
            }
        };
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

}
