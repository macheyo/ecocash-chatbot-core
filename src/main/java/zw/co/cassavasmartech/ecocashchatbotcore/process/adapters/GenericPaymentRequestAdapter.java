package zw.co.cassavasmartech.ecocashchatbotcore.process.adapters;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.ecocashchatbotcore.common.EBSProcess;
import zw.co.cassavasmartech.ecocashchatbotcore.payment.PaymentRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.payment.PaymentResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.payment.PaymentService;
import zw.co.cassavasmartech.ecocashchatbotcore.process.ProcessConstants;


@Slf4j
@Component
public class GenericPaymentRequestAdapter implements JavaDelegate {

    private final PaymentService paymentService;
    private final EBSProcess ebsProcess;

    public GenericPaymentRequestAdapter(PaymentService paymentService, EBSProcess ebsProcess) {
        this.paymentService = paymentService;
        this.ebsProcess = ebsProcess;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        PaymentRequest paymentRequest = (PaymentRequest) delegateExecution.getVariable(ProcessConstants.PAYMENT_REQUEST);
        PaymentResponse paymentResponse = getPaymentResponse(paymentRequest);
        if (paymentResponse.getResponseCode() == (HttpStatus.OK.value())) {
            delegateExecution.setVariable(ProcessConstants.PAYMENT_RESPONSE, paymentResponse);
        } else if (paymentResponse.getResponseCode() == 603) {
            //Do nothing here
        } else {
            ebsProcess.stopProcess(delegateExecution, paymentResponse);
        }
    }

    private PaymentResponse getPaymentResponse(PaymentRequest paymentRequest) {
        return paymentService.processPaymentRequest(paymentRequest);
    }

}