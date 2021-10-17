//package zw.co.cassavasmartech.ecocashchatbotcore.payment.cpo;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.RequestEntity;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.util.CollectionUtils;
//import org.springframework.web.client.RestTemplate;
//import zw.co.cassavasmartech.esb.commons.enums.PaymentType;
//import zw.co.cassavasmartech.esb.commons.enums.TransactionStatus;
//import zw.co.cassavasmartech.esb.model.PaymentRecord;
//import zw.co.cassavasmartech.esb.model.Transaction;
//import zw.co.cassavasmartech.esb.payment.PaymentRequestProcessor;
//import zw.co.cassavasmartech.esb.payment.PaymentResponse;
//import zw.co.cassavasmartech.esb.payment.cpo.data.CardPaymentResponse;
//import zw.co.cassavasmartech.esb.payment.cpo.data.LookupRequest;
//import zw.co.cassavasmartech.esb.paymentresponse.PaymentRecordService;
//import zw.co.cassavasmartech.esb.transaction.TransactionService;
//
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Slf4j
//public class CpoRequestPaymentRequestProcessorImpl implements PaymentRequestProcessor {
//    @Value("${cpo.lookup.url}")
//    private String lookupUrl;
//    @Autowired
//    private RestTemplate restTemplate;
//    @Autowired
//    private CpoHelper cpoHelper;
//    @Autowired
//    private TransactionService transactionService;
//    @Autowired
//    private PaymentRecordService paymentRecordService;
//
//    //todo : lookup record on cpo and save to payment-record table
//
//    @Override
//    public PaymentResponse processPayment(Transaction transaction) {
//        PaymentResponse paymentResponse;
//        final LookupRequest lookupRequest = buildLookupRequest(transaction);
//        final Optional<PaymentRecord> paymentRecordOptional = paymentRecordService.findByRequestReference(transaction.getSourceReference());
//        if (paymentRecordOptional.isPresent()) {
//            paymentResponse = buildErrorResponse(transaction.getPaymentReference(), "This card payment was used for a previous purchase.");
//        } else {
//            try {
//                paymentResponse = invokeCpo(lookupRequest, transaction);
//            } catch (Exception e) {
//                log.error("Error thrown while confirming CPO Card payment :{}", e);
//                paymentResponse = buildErrorResponse(transaction.getPaymentReference(), e.getMessage());
//            }
//        }
//        return paymentResponse;
//    }
//
////    @Override
////    public PaymentResponse reversePayment(Transaction transaction) {
////        transaction.setTranStatus(TransactionStatus.PENDING_MANUAL_REVERSAL);
////        transactionService.save(transaction);
////        PaymentResponse response = new PaymentResponse(transaction.getPaymentReference());
////        response.setCallBackUrl(transaction.getCallBackUrl());
////        response.setSourceReference(transaction.getSourceReference());
////        response.setReceiptNumber(transaction.getReceiptNumber());
////        response.setTransactionStatus(transaction.getTranStatus());
////        response.setResponseMessage("Requires manual reversal. Selected payment method has no real time reversals enabled");
////        return response;
////    }
//
//
//    private PaymentResponse invokeCpo(LookupRequest lookupRequest, Transaction transaction) throws Exception {
//        final RequestEntity<LookupRequest> requestEntity = new RequestEntity<>(lookupRequest, HttpMethod.POST, new URI(lookupUrl));
//        log.info("\n\nCpo Request: {}", requestEntity);
//        final ResponseEntity<List<CardPaymentResponse>> responseEntity = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<List<CardPaymentResponse>>() {
//        });
//        log.info("\n\nCpo Response: {}", responseEntity);
//        final List<CardPaymentResponse> responseEntityBody = responseEntity.getBody();
//        log.trace("\n\nCpo responseEntityBody: {}", responseEntityBody);
//        return buildResponse(responseEntityBody, transaction);
//    }
//
//    private PaymentResponse buildResponse(List<CardPaymentResponse> cpoResponse, Transaction transaction) {
//        PaymentResponse response = buildErrorResponse(transaction.getPaymentReference(), "");
//        if (!CollectionUtils.isEmpty(cpoResponse) && cpoResponse.size() == 1) {
//            CardPaymentResponse lookupResponse = cpoResponse.get(0);
//            response.setResponseMessage(lookupResponse.getPaymentStatus());
//            response.setReceiptNumber(lookupResponse.getPaymentReference());
//            if (cpoHelper.wasPaymentCompleted(lookupResponse.getPaymentStatus())
//                    && cpoHelper.isSameTransaction(transaction, lookupResponse)
//                    && lookupResponse.getPaymentStatus().equalsIgnoreCase("success")) {
//                response.setResponseCode(TransactionStatus.SUCCESS.getStatusCode());
//                response.setTransactionStatus(TransactionStatus.SUCCESS);
//            } else {
//                response.setResponseMessage("payment failed on the card payment platform");
//            }
//            persistLookupResult(lookupResponse, transaction, response);
//        } else {
//            response.setResponseMessage("no payment with reference " + transaction.getPaymentReference() + " was found on the card payment platform");
//        }
//        if (response.getTransactionStatus() == TransactionStatus.SUCCESS) {
//            response.setTransactionStatus(TransactionStatus.PAID);
//        }
//        return response;
//    }
//
//    private void persistLookupResult(CardPaymentResponse lookupResponse, Transaction transaction, PaymentResponse paymentResponse) {
//        PaymentRecord paymentRecord = new PaymentRecord();
//        paymentRecord.setTransactionType(transaction.getPaymentMethod().getTranType());
//        paymentRecord.setPaymentType(PaymentType.CARD);
//        paymentRecord.setRequestReference(transaction.getSourceReference());
//        paymentRecord.setResponseCode(lookupResponse.getPaymentResponseCode());
//        paymentRecord.setResponseMessage(lookupResponse.getPaymentResponseExplanation());
//        paymentRecord.setResponseData(lookupResponse.getCassavaPayId());
//        paymentRecord.setResponseReference(lookupResponse.getPaymentReference());
//        paymentRecord.setTransactionStatus(paymentResponse.getTransactionStatus());
//        paymentRecordService.save(paymentRecord);
//    }
//
//    private LookupRequest buildLookupRequest(Transaction transaction) {
//        LookupRequest lookupRequest = new LookupRequest();
//        List<String> orders = new ArrayList<>();
//        orders.add(transaction.getSourceReference());
//        List<Integer> ids = new ArrayList<>();
//        lookupRequest.setVendorId("convenience_store");//TODO : this may hv to be passed in request
//        lookupRequest.setSearchIds(ids);
//        lookupRequest.setSearchOrders(orders);
//        return lookupRequest;
//    }
//}
