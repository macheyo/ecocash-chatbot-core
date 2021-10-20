package zw.co.cassavasmartech.ecocashchatbotcore.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@Service
@Slf4j
public class ApiHeader {
    @Autowired
    private HttpSession httpSession;

    public HttpHeaders headerWithToken() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + httpSession.getAttribute("TOKEN"));
        return httpHeaders;
    }

    public HttpHeaders headerWithTokenFile() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + httpSession.getAttribute("TOKEN"));
        return httpHeaders;
    }

}
