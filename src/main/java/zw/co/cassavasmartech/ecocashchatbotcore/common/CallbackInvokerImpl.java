package zw.co.cassavasmartech.ecocashchatbotcore.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import zw.co.cassavasmartech.esb.payment.PaymentResponse;

@Slf4j
@Component
public class CallbackInvokerImpl implements CallbackInvoker {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("noproxyRestTemplate")
    private RestTemplate noProxyRestTemplate;


    @Override
    public void invoke(PaymentResponse response) {
        try {
            postPaymentConfirmation(response);
        } catch (Exception ex) {
            log.error("", ex);
        }
    }

    //todo: remove this since its not required in prod

    public String postPaymentConfirmation(final PaymentResponse paymentResponse) {
        HttpEntity<PaymentResponse> requestEntity = new HttpEntity<>(paymentResponse, this.httpHeaders());
        if(paymentResponse.getCallBackUrl()!=null && (paymentResponse.getCallBackUrl().startsWith("http://192.168")||paymentResponse.getCallBackUrl().startsWith("https://192.168"))){
            log.info("Invoker posting request outside proxy : {}", requestEntity);
        ResponseEntity<String> response = noProxyRestTemplate.exchange(paymentResponse.getCallBackUrl(), HttpMethod.POST, requestEntity, String.class);
        log.info("Invoker response: {}", response);
        return response.getBody();}
        else{
            log.info("Invoker posting request inside proxy : {}", requestEntity);
            ResponseEntity<String> response = restTemplate.exchange(paymentResponse.getCallBackUrl(), HttpMethod.POST, requestEntity, String.class);
            log.info("Invoker response: {}", response);
            return response.getBody();
        }
    }
}
