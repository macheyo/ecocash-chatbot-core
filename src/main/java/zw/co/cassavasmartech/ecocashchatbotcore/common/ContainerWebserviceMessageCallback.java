package zw.co.cassavasmartech.ecocashchatbotcore.common;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import javax.xml.transform.TransformerException;
import java.io.IOException;

public class ContainerWebserviceMessageCallback implements WebServiceMessageCallback {

    private String soapAction;


    public ContainerWebserviceMessageCallback(String soapAction){
        this.soapAction = soapAction;
    }

    @Override
    public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {
        final WebServiceMessageCallback soapActionCallback = new SoapActionCallback(soapAction);
        soapActionCallback.doWithMessage(message);
    }

}