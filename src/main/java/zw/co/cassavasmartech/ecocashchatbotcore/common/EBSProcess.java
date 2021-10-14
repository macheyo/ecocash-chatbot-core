package zw.co.cassavasmartech.ecocashchatbotcore.common;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.esb.payment.PaymentResponse;
import zw.co.cassavasmartech.esb.process.ProcessConstants;

@Component
@Slf4j
public class EBSProcess {

    @Autowired
    private ProcessEngine camunda;
    @Autowired
    private CallbackInvoker callbackInvoker;

    public void stopProcess(DelegateExecution ctx, PaymentResponse paymentResponse) {

        if (ctx != null) {
            String sourceReference = (String) ctx.getVariable(ProcessConstants.SOURCE_REFERENCE);
            String paymentReference = (String) ctx.getVariable(ProcessConstants.PAYMENT_REFERENCE);
            String callBackUrl = (String) ctx.getVariable(ProcessConstants.VAR_NAME_CALL_BACK_URL);
            paymentResponse.setSourceReference(sourceReference);
            paymentResponse.setPaymentReference(paymentReference);
            paymentResponse.setCallBackUrl(callBackUrl);
        }

        ProcessInstance nextTaskInstance = camunda.getRuntimeService()
                .createProcessInstanceQuery()
                .variableValueEquals(ProcessConstants.PAYMENT_REFERENCE, paymentResponse.getPaymentReference())
                .singleResult();
        callbackInvoker.invoke(paymentResponse);
        camunda.getRuntimeService().deleteProcessInstance(nextTaskInstance.getId(),
                String.format("%d:%s", paymentResponse.getResponseCode(), paymentResponse.getResponseMessage()));
    }

}
