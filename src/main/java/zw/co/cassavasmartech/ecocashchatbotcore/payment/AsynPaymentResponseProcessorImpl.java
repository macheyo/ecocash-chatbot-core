//package zw.co.cassavasmartech.ecocashchatbotcore.payment;
//
//import lombok.extern.slf4j.Slf4j;
//import org.camunda.bpm.engine.ProcessEngine;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import zw.co.cassavasmartech.esb.commons.EBSProcess;
//import zw.co.cassavasmartech.esb.model.Transaction;
//import zw.co.cassavasmartech.esb.process.ProcessConstants;
//
///**
// * Created by mtakayindisa on 12/04/2019.
// */
//
//@Slf4j
//@Service
//public class AsynPaymentResponseProcessorImpl implements PaymentResponseProcessor {
//
//    @Autowired
//    private ProcessEngine camunda;
//    @Autowired
//    private EBSProcess ebsProcess;
//
//    @Override
//    public PaymentResponse processResponse(Transaction transaction) {
//        log.info("Transaction response: {}", transaction);
//        final PaymentResponse response = new PaymentResponse(transaction.getPaymentReference());
//        response.setReceiptNumber(transaction.getReceiptNumber());
//        response.setTransactionStatus(transaction.getTranStatus());
//        response.setResponseMessage(transaction.getStatusReason());
//        response.setResponseCode(transaction.getTranStatus().getStatusCode());
//        response.setCallBackUrl(transaction.getCallBackUrl());
//        continueNextTask(response, transaction.getProcessRegister().getMessageCorrelation());
//        return response;
//    }
//
//    private void continueNextTask(PaymentResponse paymentResponse, String messageName) {
//
//        if (paymentResponse.getResponseCode() == (HttpStatus.OK.value())) {
//            camunda.getRuntimeService()
//                    .createMessageCorrelation(messageName)
//                    .processInstanceBusinessKey(paymentResponse.getPaymentReference())
//                    //.processInstanceVariableEquals(ProcessConstants.PAYMENT_REFERENCE,paymentResponse.getPaymentReference())
//                    .setVariable(ProcessConstants.PAYMENT_RESPONSE, paymentResponse)
//                    .correlateWithResult();
//        } else {
//            ebsProcess.stopProcess(null, paymentResponse);
//        }
//    }
//}
