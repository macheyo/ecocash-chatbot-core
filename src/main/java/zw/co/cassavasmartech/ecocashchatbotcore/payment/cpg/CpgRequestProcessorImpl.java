package zw.co.cassavasmartech.ecocashchatbotcore.payment.cpg;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.esb.commons.MobileNumberFormater;
import zw.co.cassavasmartech.esb.commons.data.PostTransaction;
import zw.co.cassavasmartech.esb.commons.data.PostTransactionResponse;
import zw.co.cassavasmartech.esb.commons.data.Response;
import zw.co.cassavasmartech.esb.commons.data.TransactionRequest;
import zw.co.cassavasmartech.esb.payment.cpg.data.MerchantToMerchantPaymentRequest;
import zw.co.cassavasmartech.esb.payment.cpg.data.MerchantToSubscriberPaymentRequest;
import zw.co.cassavasmartech.esb.payment.cpg.data.SubscriberToMerchantRequest;

import java.util.Date;

@Service
@Slf4j
public class CpgRequestProcessorImpl implements CpgRequestProcessor {

    @Autowired
    private PaymentGatewayEndpointInvoker endpointInvoker;

    @Autowired
    MobileNumberFormater mobileNumberFormater;

    @Autowired
    ChecksumGenerator checksumGenerator;

    @Value("${esb.cpg.endpoint.url}")
    private String endpointUrl;

    @Value("${esb.cpg.vendor}")
    private String vendorCode;

    @Value("${esb.cpg.vendor.key}")
    private String vendorApiKey;

    @Value("${esb.cpg.customer.lookup.tran.type}")
    private String customerLookUpTranType;

    @Value("${esb.merchant.to.merchant.trans.type.code}")
    private String merchantToMerchantTransTypeCode;

    @Value("${esb.merchant.to.subscriber.trans.type.code}")
    private String merchantToSubscriberTransTypeCode;

    @Value("${esb.subscriber.to.merchant.trans.type.code}")
    private String ubscriberToMerchantTransTypeCode;

    @Override
    public Response customerLookUp(String msisdn) throws Exception {
        final String formatedMsisdn = mobileNumberFormater.formatMsisdnMinimum(msisdn);
        final TransactionRequest customerLookUpRequest = buildCustomerLookupRequest(formatedMsisdn);
        return getInvoke(customerLookUpRequest);
    }

    @Override
    public Response merchantToMerchantPayment(MerchantToMerchantPaymentRequest merchantToMerchantPaymentRequest) throws Exception {
        final TransactionRequest transactionRequest = buildMerchantToMerchantRequest(merchantToMerchantPaymentRequest);
        return getInvoke(transactionRequest);

    }

    @Override
    public Response merchantToSubscriber(MerchantToSubscriberPaymentRequest merchantToSubscriberPaymentRequest) throws Exception {
        TransactionRequest transactionRequest = buildMerchantToSubscriberRequest(merchantToSubscriberPaymentRequest);
        return getInvoke(transactionRequest);
    }

    @Override
    public Response subscriberToMerchant(SubscriberToMerchantRequest subscriberToMerchantRequest) throws Exception {
        TransactionRequest transactionRequest = buildSubscriberToMerchantRequest(subscriberToMerchantRequest);
        return getInvoke(transactionRequest);
    }

    public TransactionRequest buildMerchantToSubscriberRequest(MerchantToSubscriberPaymentRequest request) throws Exception {
        return new RequestBuilder()
                .vendorCode(vendorCode)
                .vendorApiKey(vendorApiKey)
                .checksumGenerator(checksumGenerator)
                .currency(request.getCurrency())
                .amount(String.valueOf(request.getAmount()))
                .tranType(merchantToSubscriberTransTypeCode)
                .msisdn(request.getMerchantMsisdn())
                .msisdn2(request.getSubscriberMsisdn())
                .pin(request.getMerchantPin())
                .reference(request.getSourceRef())
                .build();

    }

    public TransactionRequest buildSubscriberToMerchantRequest(SubscriberToMerchantRequest request) throws Exception {
        return new RequestBuilder()
                .vendorCode(vendorCode)
                .vendorApiKey(vendorApiKey)
                .checksumGenerator(checksumGenerator)
                .currency(request.getCurrency())
                .amount(String.valueOf(request.getAmount()))
                .tranType(ubscriberToMerchantTransTypeCode)
                .msisdn2(request.getMerchantMsisdn())
                .msisdn(request.getSubscriberMsisdn())
                .reference(request.getSourceRef())
                .pin(request.getPin())
                .build();

    }

    private TransactionRequest buildMerchantToMerchantRequest(MerchantToMerchantPaymentRequest merchantToMerchantPaymentRequest) throws Exception {
        return new RequestBuilder()
                .vendorCode(vendorCode)
                .vendorApiKey(vendorApiKey)
                .checksumGenerator(checksumGenerator)
                .tranType(merchantToMerchantTransTypeCode)
                .currency(merchantToMerchantPaymentRequest.getCurrency())
                .msisdn(mobileNumberFormater.formatMsisdnMinimum(merchantToMerchantPaymentRequest.getSourceMerchantMsisdn()))
                .accountNumber(merchantToMerchantPaymentRequest.getSourceMerchantCode())
                .msisdn2(mobileNumberFormater.formatMsisdnMinimum(merchantToMerchantPaymentRequest.getDestinationMerchantMsisdn()))
                .reference(merchantToMerchantPaymentRequest.getSourceRef())
                .pin(merchantToMerchantPaymentRequest.getMerchantPin())
                .amount(String.valueOf(merchantToMerchantPaymentRequest.getAmount()))
                .build();
    }

    private TransactionRequest buildCustomerLookupRequest(String msisdn) throws Exception {
        return new RequestBuilder()
                .vendorCode(vendorCode)
                .vendorApiKey(vendorApiKey)
                .reference(checksumGenerator.generateReference(msisdn, new Date()))
                .checksumGenerator(checksumGenerator)
                .msisdn(msisdn).tranType(customerLookUpTranType).build();
    }

    private Response getInvoke(TransactionRequest transactionRequest) {
        final PostTransaction postTransaction = new PostTransaction();
        postTransaction.setTransactionRequest(transactionRequest);
        log.trace("CPG Transaction Request {}", transactionRequest);
        final PostTransactionResponse response = endpointInvoker.invoke(postTransaction, endpointUrl);
        log.trace("CPG Transaction Response {}", response);
        return response.getReturn();
    }


}
