package zw.co.cassavasmartech.ecocashchatbotcore.cpg;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import zw.co.cassavasmartech.ecocashchatbotcore.common.Util;
import zw.co.cassavasmartech.ecocashchatbotcore.config.CpgConfigurationProperties;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.data.MerchantToMerchantRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.data.MerchantToSubscriberRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.data.SubscriberToMerchantRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.data.SubscriberToSubscriberRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.exception.BusinessException;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PostTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PostTransactionResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionResponse;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentGatewayProcessorImpl implements PaymentGatewayProcessor {

    private final CheckSumGenerator checksumGenerator;
    private final PaymentGatewayInvoker paymentGatewayInvoker;
    private final CpgConfigurationProperties cpgConfigProperties;
    @Value("${ecocash.chatbot.core.cpg-api.vendorGIGAIOTCode}")
    private String vendorGIGAIOTCode;
    @Value("${ecocash.chatbot.core.cpg-api.vendorGIGAIOTCode}")
    private String vendorGIGAIOTApiKey;
    @Value("${ecocash.chatbot.core.cpg-api.vendorEPGCode}")
    private String vendorEPGCode;
    @Value("${ecocash.chatbot.core.cpg-api.vendorEPGApiKey}")
    private String vendorEPGApiKey;


    @Override
    public TransactionResponse merchantToSubscriber(MerchantToSubscriberRequest merchantToSubscriberRequest) {
        return invokeApi(getMerchantTosubscriberRequest(merchantToSubscriberRequest));
    }

    @Override
    public TransactionResponse subscriberToMerchant(SubscriberToMerchantRequest subscriberToMerchantRequest) {
        final TransactionRequest transactionRequest = getTransactionRequest(subscriberToMerchantRequest);
        log.debug("Processing subscriber to merchant request");
        return invokeApi(transactionRequest);

    }

    @Override
    public TransactionResponse merchantToMerchant(MerchantToMerchantRequest merchantToMerchantRequest) {
        final TransactionRequest transactionRequest = getMerchantToMerchantRequest(merchantToMerchantRequest);
        log.debug("Processing subscriber to merchant request");
        return invokeApi(transactionRequest);
    }

    @Override
    public TransactionResponse pinReset(String msisdn) {
        final TransactionRequest transactionRequest = getPinResetRequest(msisdn);
        log.debug("Processing Pin Reset request");
        return invokeApi(transactionRequest);
    }

    @Override
    public TransactionResponse lookupCustomer(String msisdn) {
        final TransactionRequest transactionRequest = getLookUpCustomerRequest(msisdn);
        log.debug("Processing Lookup Customer request");
        return invokeApi(transactionRequest);
    }

    @Override
    public TransactionResponse subscriberToSubscriber(SubscriberToSubscriberRequest subscriberToSubscriberRequest) {
        final TransactionRequest transactionRequest = getSubscriberToSubscriberRequest(subscriberToSubscriberRequest);
        log.debug("Processing send money Peer to Peer");
        return invokeApi(transactionRequest);
    }



    @Override
    public TransactionResponse getStatement(String msisdn) {
        final TransactionRequest transactionRequest = getStatementRequest(msisdn);
        log.debug("Processing get Statement request");
        return invokeApi(transactionRequest);
    }


    private TransactionResponse invokeApi(TransactionRequest transactionRequest) {
        final PostTransaction postTransaction = new PostTransaction();
        postTransaction.setTransactionRequest(transactionRequest);
        return paymentGatewayInvoker.invoke(postTransaction)
                .map(PostTransactionResponse::getTransactionResponse)
                .orElseThrow(() -> new BusinessException("Received null response from payment gateway"));
    }



    private TransactionRequest getLookUpCustomerRequest(String msisdn) {
        return RequestBuilder.newInstance()
                .vendorCode(vendorGIGAIOTCode)
                .checksumGenerator(checksumGenerator)
                .vendorApiKey(vendorGIGAIOTApiKey)
                .checksum("checksum")
                .tranType(cpgConfigProperties.getCustomerLookupTranType())
                .applicationCode("ecocashzw")
                .reference(Util.generateReference(msisdn))
                .msisdn(msisdn)
                .currency("ZWL")
                .build();
    }

    private TransactionRequest getStatementRequest(String msisdn) {
        return RequestBuilder.newInstance()
                .vendorCode("EPGTESTPT")
                .checksumGenerator(checksumGenerator)
                .tranType("00029")
                .applicationCode("ecocashzw")
                .reference(Util.generateReference(msisdn))
                .securityMode("101")
                .vendorApiKey("abf8988717c777874645af9e60db6e607dd5962c6e9c821f775c515618d2393e")
                .msisdn(msisdn)
                .build();
    }

    private TransactionRequest getTransactionRequest(SubscriberToMerchantRequest request) {
        return RequestBuilder.newInstance()
                .vendorCode(vendorGIGAIOTCode)
                .vendorApiKey(vendorGIGAIOTApiKey)
                .checksumGenerator(checksumGenerator)
                .currency("ZWL")
                .amount(String.valueOf(request.getAmount()))
                .tranType(cpgConfigProperties.getSubscriberToMerchantTranType())
                .msisdn2(request.getMerchantMsisdn())
                .msisdn(request.getSubscriberMsisdn())
                .reference(Util.generateReference(request.getSubscriberMsisdn()))
                .applicationCode("ecocash")
                .build();
    }

// PEER TO PEER
    private TransactionRequest getSubscriberToSubscriberRequest(SubscriberToSubscriberRequest request) {
        return RequestBuilder.newInstance()
                .vendorCode(vendorGIGAIOTCode)
                .vendorApiKey(vendorGIGAIOTApiKey)
                .msisdn(request.getMsisdn1())
                .checksumGenerator(checksumGenerator)
                .tranType(cpgConfigProperties.getSubscriberToSubscriberTranType())
                .applicationCode("ecocashzw")
                .reference(Util.generateReference(request.getMsisdn1()))
                .msisdn2(request.getMsisdn2())
                .amount(String.valueOf(request.getAmount()))
                .currency("RTGS")
                .countryCode("ZW")
                .build();

    }



    private TransactionRequest getMerchantTosubscriberRequest(MerchantToSubscriberRequest request) {
        return RequestBuilder.newInstance()
                .vendorCode(vendorGIGAIOTCode)
                .vendorApiKey(vendorGIGAIOTApiKey)
                .checksumGenerator(checksumGenerator)
                .currency(request.getCurrency())
                .amount(String.valueOf(request.getAmount()))
                .tranType(cpgConfigProperties.getMerchantToSubscriberTranType())
                .msisdn(request.getMerchantMsisdn())
                .msisdn2(request.getSubscriberMsisdn())
                .pin(request.getMerchantPin())
                .reference(request.getSourceRef())
                .build();
    }

    private TransactionRequest getMerchantToMerchantRequest(MerchantToMerchantRequest request) {
        return RequestBuilder.newInstance()
                .vendorCode(vendorGIGAIOTCode)
                .vendorApiKey(vendorGIGAIOTApiKey)
                .checksumGenerator(checksumGenerator)
                .currency(request.getCurrency())
                .amount(String.valueOf(request.getAmount()))
                .tranType(cpgConfigProperties.getMerchantToSubscriberTranType())
                .msisdn(request.getSourceMerchantMsisdn())
                .msisdn2(request.getDestinationMsisdn())
                .pin(request.getSourceMerchantPin())
                .reference(request.getSourceRef())
                .build();
    }

    private TransactionRequest getPinResetRequest(String msisdn) {
        return RequestBuilder.newInstance()
                .vendorCode(vendorGIGAIOTCode)
                .vendorApiKey(vendorGIGAIOTApiKey)
                .checksumGenerator(checksumGenerator)
                .tranType(cpgConfigProperties.getPinResetTranType())
                .msisdn(msisdn)
                .reference(Util.generateReference(msisdn))
                .build();
    }
}
