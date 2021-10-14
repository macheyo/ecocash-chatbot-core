package zw.co.cassavasmartech.ecocashchatbotcore.payment.ecocash.eip;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import zw.co.cassavasmartech.esb.commons.enums.PaymentType;
import zw.co.cassavasmartech.esb.commons.enums.TransactionStatus;
import zw.co.cassavasmartech.esb.model.PartnerPspDetail;
import zw.co.cassavasmartech.esb.model.PaymentRecord;
import zw.co.cassavasmartech.esb.model.Transaction;
import zw.co.cassavasmartech.esb.partnerpspdetail.PartnerPspDetailService;
import zw.co.cassavasmartech.esb.payment.PaymentRequestProcessor;
import zw.co.cassavasmartech.esb.payment.PaymentResponse;
import zw.co.cassavasmartech.esb.payment.ecocash.eip.data.*;
import zw.co.cassavasmartech.esb.paymentresponse.PaymentRecordService;
import zw.co.cassavasmartech.esb.transaction.TransactionService;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Optional;

@Service
@Slf4j
public class SubscriberToMerchantPaymentRequestProcessorImpl implements PaymentRequestProcessor {

    @Autowired
    private Environment environment;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PaymentRecordService paymentRecordService;

    @Autowired
    private TransactionService transactionService;

    @Value("${esb.ecocash.api.security.username}")
    private String apiUserName;
    @Value("${esb.ecocash.api.security.password}")
    private String apiPassword;
    @Autowired
    private PartnerPspDetailService partnerPspDetailService;

    public PaymentResponse processPayment(Transaction transaction) {
        try {
            log.info("Initiating payment for request {}", transaction);
            final AmountTransaction merchantReq = new AmountTransaction();
            populateAmountTransaction(transaction, merchantReq);
            final String eipEndpointUrl = environment.getProperty("moovah.ecocash.api.initiate.payment.url");
            PaymentResponse paymentResponse = invokeEip(merchantReq, eipEndpointUrl);
            savePaymentRecord(paymentResponse, transaction);
            return paymentResponse;
        } catch (Exception e) {
            log.error("Error Processing Request.", e);
            return buildErrorResponse(transaction.getPaymentReference(), e.getMessage());
        }
    }

//    @Override
//    public PaymentResponse reversePayment(Transaction transaction) {
//        try {
//            final AmountTransactionRefund refundRequest = new AmountTransactionRefund();
//            populateAmountTransactionReversal(transaction, refundRequest);
//            final String refundUrl = environment.getProperty("moovah.ecocash.api.refund.transaction.url");
//            PaymentResponse paymentResponse = invokeEip(refundRequest, refundUrl);
//            persistReversal(transaction, paymentResponse);
//            updateTransaction(transaction, paymentResponse);
//            return paymentResponse;
//        } catch (Exception e) {
//            log.error("Error Processing Request.", e);
//            return buildErrorResponse(transaction.getPaymentReference(), e.getMessage());
//        }
//    }

    @Override
    public PaymentResponse checkPaymentStatus(final Transaction transaction) {
        try {
            final String transactionStatusQueryUrl = environment.getProperty("moovah.ecocash.api.query.transaction");
            String queryTransactionUri = String.format(transactionStatusQueryUrl,
                    transaction.getMsisdn1(), transaction.getPaymentReference());
            log.debug("Transaction URL {} ", queryTransactionUri);

            final RequestEntity<AmountTransaction> requestEntity = new RequestEntity<>(addBasicAuthentication(apiUserName, apiPassword), HttpMethod.GET, new URI(queryTransactionUri));
            log.info("\n\nEip Check Status Request: {}", requestEntity);
            final ResponseEntity<AmountTransaction> responseEntity = restTemplate.exchange(requestEntity, AmountTransaction.class);

            final AmountTransaction amountTransaction = responseEntity.getBody();
            log.info("EIP Check Status Response {} ", amountTransaction);
            final AmountTransaction eipResponse = amountTransaction;
            return EipUtils.buildPaymentResponse(eipResponse, transaction.getPaymentReference());
        } catch (Exception e) {
            log.error("EIP lookup status :{}", e);
            return buildErrorResponse(transaction.getPaymentReference(), "EIP check status API not available");
        }
    }


    private PaymentResponse updateTransaction(Transaction transaction, PaymentResponse paymentResponse) {

        if (paymentResponse.getTransactionStatus() != TransactionStatus.SUCCESS) {
            paymentResponse.setTransactionStatus(TransactionStatus.PENDING_MANUAL_REVERSAL);
            paymentResponse.setResponseCode(TransactionStatus.PENDING_MANUAL_REVERSAL.getStatusCode());
            paymentResponse.setResponseMessage("refund failed. " + paymentResponse.getResponseMessage());
        } else {
            paymentResponse.setTransactionStatus(TransactionStatus.REVERSED);
            paymentResponse.setResponseCode(TransactionStatus.REVERSED.getStatusCode());
            paymentResponse.setResponseMessage(TransactionStatus.REVERSED.toString());
        }
        transaction.setTranStatus(paymentResponse.getTransactionStatus());
        transactionService.save(transaction);
        return paymentResponse;
    }

    private void savePaymentRecord(PaymentResponse paymentResponse, Transaction transaction) {
        PaymentRecord paymentRecord = new PaymentRecord();
        paymentRecord.setTransaction(transaction);
        paymentRecord.setTransactionStatus(paymentResponse.getTransactionStatus());
        paymentRecord.setPaymentType(PaymentType.ECOCASH);
        paymentRecord.setRequestReference(transaction.getPaymentReference());
        paymentRecord.setResponseCode(String.valueOf(paymentResponse.getResponseCode()));
        paymentRecord.setResponseMessage(paymentResponse.getResponseMessage());
        paymentRecord.setResponseReference(paymentResponse.getReceiptNumber());
        paymentRecord.setTransactionType(transaction.getPaymentMethod().getTranType());
        paymentRecordService.save(paymentRecord);
        if (paymentResponse.getTransactionStatus() == TransactionStatus.SUCCESS) {
            paymentResponse.setTransactionStatus(TransactionStatus.PAID);
        }
    }

    private void persistReversal(Transaction transaction, PaymentResponse paymentResponse) {
        PaymentRecord refundEntity = new PaymentRecord();
        refundEntity.setTransaction(transaction);
        refundEntity.setRequestReference(new StringBuilder(transaction.getPaymentReference()).append("REF").toString());
        refundEntity.setTransactionStatus(paymentResponse.getTransactionStatus());
        refundEntity.setResponseReference(paymentResponse.getReceiptNumber());
        refundEntity.setResponseMessage(paymentResponse.getResponseMessage());
        refundEntity.setResponseCode(String.valueOf(paymentResponse.getTransactionStatus().getStatusCode()));
        refundEntity.setPaymentType(PaymentType.ECOCASH_REFUND);
        refundEntity.setTransactionType(transaction.getPaymentMethod().getTranType().getReversalTranType());
        final Optional<PaymentRecord> paymentRecordOptional = paymentRecordService.save(refundEntity);
        PaymentRecord savedReversalRecord = paymentRecordOptional.get();
        final Optional<PaymentRecord> paymentRecordLookupResult
                = paymentRecordService.findByTransactionAndTransactionType(transaction, transaction.getPaymentMethod().getTranType());
        PaymentRecord originalPaymentRecord = paymentRecordLookupResult.get();
        originalPaymentRecord.setReversalRecord(savedReversalRecord);
        paymentRecordService.update(originalPaymentRecord);
    }

    private PaymentResponse invokeEip(AmountTransaction amountTransaction, String endpointUrl) throws URISyntaxException {
        final RequestEntity<AmountTransaction> requestEntity = new RequestEntity<>(amountTransaction, addBasicAuthentication(apiUserName, apiPassword), HttpMethod.POST, new URI(endpointUrl));
        log.info("\n\nEip Request for txn {} is: {}", amountTransaction.getClientCorrelator(), requestEntity);
        final ResponseEntity<AmountTransaction> responseEntity = restTemplate.exchange(requestEntity, AmountTransaction.class);
        log.info("\n\nEip Response for txn {} is : {}", amountTransaction.getClientCorrelator(),responseEntity);
        final AmountTransaction responseEntityBody = responseEntity.getBody();
        final AmountTransaction eipResponse = responseEntityBody;
        return EipUtils.buildPaymentResponse(eipResponse, amountTransaction.getReferenceCode());
    }

    private void populateAmountTransaction(Transaction transaction, AmountTransaction merchantReq) throws Exception {
        final Optional<PartnerPspDetail> partnerPspDetailOptional = partnerPspDetailService.findByPartnerAndTranType(transaction.getPartner(), transaction.getProcessRegister().getTranType());
        final ChargingInformation chargingInformation = buildChargingInformation(transaction);
        final ChargeMetaData chargeMetaData = new ChargeMetaData(transaction.getChannel().name());
        final PaymentAmount paymentAmount = getPaymentAmount(chargingInformation, chargeMetaData);
        if (partnerPspDetailOptional.isPresent()) {
            final PartnerPspDetail partnerPspDetail = partnerPspDetailOptional.get();
            merchantReq.setMerchantCode(partnerPspDetail.getMerchantCode());
            merchantReq.setMerchantName(partnerPspDetail.getMerchantName());
            merchantReq.setMerchantNumber(partnerPspDetail.getMerchantMsisdn());
            merchantReq.setMerchantPin(partnerPspDetail.getMerchantPin());
            merchantReq.setTerminalID(partnerPspDetail.getTerminalId());
            merchantReq.setLocation(partnerPspDetail.getLocation());
            merchantReq.setSuperMerchantName(partnerPspDetail.getMerchantSuperName());
            merchantReq.setRemarks(partnerPspDetail.getRemark());
            merchantReq.setCountryCode(environment.getRequiredProperty("moovah.ecocash.api.params.countryCode"));
            merchantReq.setCurrencyCode(transaction.getCurrency().getCode());
            merchantReq.setEndUserId(transaction.getMsisdn1());
            merchantReq.setClientCorrelator(transaction.getPaymentReference());
            merchantReq.setReferenceCode(transaction.getPaymentReference());
            merchantReq.setNotifyUrl(environment.getRequiredProperty("moovah.ecocash.api.params.notifyUrl"));
            merchantReq.setTranType(environment.getRequiredProperty("moovah.ecocash.api.params.transactionType"));
            merchantReq.setTransactionOperationStatus(TransactionOperationStatus.CHARGED.getValue());
            merchantReq.setPaymentAmount(paymentAmount);
        } else {
            log.warn("No merchant defined for this txn type >>>>>>>> :{} >>>>>>>>>>>>>>>>{}",transaction.getPartner(), transaction.getProcessRegister().getTranType().getId());
//            throw new Exception("No merchant defined for this partner");
        }
    }

    private void populateAmountTransactionReversal(Transaction transaction, AmountTransactionRefund merchantReq) throws Exception {
        populateAmountTransaction(transaction, merchantReq);
        merchantReq.setClientCorrelator(new StringBuilder(transaction.getPaymentReference()).append("REF").toString());
        merchantReq.setReferenceCode(new StringBuilder(transaction.getPaymentReference()).append("REF").toString());
        merchantReq.setOriginalEcocashReference(transaction.getReceiptNumber());
        merchantReq.setOriginalServerReferenceCode(transaction.getPaymentReference());
        merchantReq.setTranType(environment.getRequiredProperty("moovah.ecocash.api.params.refund.transactionType"));
        merchantReq.setTransactionOperationStatus(TransactionStatus.PENDING.toString());
        merchantReq.setRemark("refund");
        merchantReq.setRemarks("refund");
    }

    private PaymentAmount getPaymentAmount(ChargingInformation chargingInformation, ChargeMetaData chargeMetaData) {
        final PaymentAmount paymentAmount = new PaymentAmount();
        paymentAmount.setCharginginformation(chargingInformation);
        paymentAmount.setChargeMetaData(chargeMetaData);
        return paymentAmount;
    }

    private ChargingInformation buildChargingInformation(Transaction transaction) {
        ChargingInformation chargingInformation = new ChargingInformation();
        chargingInformation.setCurrency(transaction.getCurrency().getCode());
        chargingInformation.setAmount(transaction.getAmount());
        chargingInformation.setDescription("");
        return chargingInformation;
    }

    public static HttpHeaders addBasicAuthentication(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }
}
