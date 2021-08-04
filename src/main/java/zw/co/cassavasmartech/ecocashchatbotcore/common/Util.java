package zw.co.cassavasmartech.ecocashchatbotcore.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

}
