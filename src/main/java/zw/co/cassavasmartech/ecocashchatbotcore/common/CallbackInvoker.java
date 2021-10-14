package zw.co.cassavasmartech.ecocashchatbotcore.common;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import zw.co.cassavasmartech.esb.payment.PaymentResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomas on 5/11/18.
 */
public interface CallbackInvoker {

    default HttpHeaders httpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.APPLICATION_JSON);
        list.add(MediaType.APPLICATION_XML);
        httpHeaders.setAccept(list);
        return httpHeaders;
    }

    void invoke(PaymentResponse response);
}
