package zw.co.cassavasmartech.ecocashchatbotcore.process.adapters;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.esb.commons.CallbackInvoker;
import zw.co.cassavasmartech.esb.payment.PaymentResponse;
import zw.co.cassavasmartech.esb.process.ProcessConstants;

@Slf4j
@Component
public class SuccessfulProcessCallBackResponseAdapter implements JavaDelegate {

    @Autowired
    private CallbackInvoker callbackInvoker;

    @Override
    public void execute(DelegateExecution ctx) throws Exception {

        PaymentResponse paymentResponse = (PaymentResponse) ctx.getVariable(ProcessConstants.PAYMENT_RESPONSE);
        String sourceReference = (String) ctx.getVariable(ProcessConstants.SOURCE_REFERENCE);
        String paymentReference = (String) ctx.getVariable(ProcessConstants.PAYMENT_REFERENCE);
        paymentResponse.setSourceReference(sourceReference);
        paymentResponse.setPaymentReference(paymentReference);
        log.trace("final Payment response sent in callback:{}",paymentResponse);
        callbackInvoker.invoke(paymentResponse);
    }
}