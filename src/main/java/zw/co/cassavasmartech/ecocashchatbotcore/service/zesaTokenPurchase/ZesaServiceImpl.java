package zw.co.cassavasmartech.ecocashchatbotcore.service.zesaTokenPurchase;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.common.CpgChecksumGenerator;
import zw.co.cassavasmartech.ecocashchatbotcore.common.CpgHelper;
import zw.co.cassavasmartech.ecocashchatbotcore.common.ServiceResponseEntityHelper;
import zw.co.cassavasmartech.ecocashchatbotcore.common.TransactionDaoHelper;
import zw.co.cassavasmartech.ecocashchatbotcore.common.data.cpg.ChecksumRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.common.data.cpg.ChecksumResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.common.enums.TransactionStatus;
import zw.co.cassavasmartech.ecocashchatbotcore.model.ServiceAllocationRecord;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Transaction;
import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.payment.PaymentResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.serviceresponse.ServiceResponseService;

import java.math.BigDecimal;

@Slf4j
@Service
public class ZesaServiceImpl implements ZesaService {
    @Value("${zesa.prepaid.cpg.uri}")
    private String zesaApiUrl;
    @Value("${zesa.prepaid.cpg.transaction.type}")
    private String zesaTxnType;
    @Value("${zesa.prepaid.cpg.vendor.code}")
    private String vendorCode;
    @Value("${zesa.prepaid.cpg.vendor.key}")
    private String vendorKey;
    @Value("${zesa.prepaid.cpg.application.code}")
    private String applicationCode;
    @Value("${zesa.prepaid.cpg.keystore.pass}")
    private String keystorePass;
    @Value("${zesa.prepaid.cpg.keystore.location}")
    private String keystoreLocation;
    @Value("${zesa.prepaid.cpg.keystore.alias}")
    private String keystoreAlias;
    @Value("${zesa.prepaid.cpg.keystore.type}")
    private String keyStoreType;
    @Value("${spring.profiles.active:}")
    private String activeProfiles;
    @Autowired
    private CpgHelper cpgHelper;
    @Autowired
    private ServiceResponseService responseService;
    @Autowired
    private ServiceResponseEntityHelper responseHelper;
    @Autowired
    private TransactionDaoHelper transactionDaoHelper;

    @Override
    public PaymentResponse postPayment(ServiceAllocationRecord serviceAllocationRecord) {

        PaymentResponse paymentResponse;
        try {
            Transaction transaction = serviceAllocationRecord.getTransaction();
            TransactionRequest zesaRequest = buildZesaTokenRequest(transaction);

            TransactionResponse response = null;
            if (isProductionProfile()) {
                response = cpgHelper.invokeApi(zesaRequest, zesaApiUrl);
                log.info(">>>>>>Zesa request :{}\n\n Zesa response :{}", zesaRequest, response);
            } else {
                response = buildDummyResponse(transaction);
                log.info(">>>>>>Simulate Zesa request :{}\n\n Simulate Zesa response :{}", zesaRequest, response);
            }
            final ServiceAllocationRecord serviceResponse = responseHelper.updateServiceResponseEntity(response, serviceAllocationRecord);
            paymentResponse = buildResponse(response, serviceResponse);
            persistAirtimeResponse(transaction, serviceResponse, paymentResponse);
            return paymentResponse;
        } catch (Exception e) {
            log.error("Exception thrown handling ZESA token request :{}", e);
            serviceAllocationRecord.setTransactionStatus(TransactionStatus.PENDING_MANUAL_REVERSAL);
            serviceAllocationRecord.setResponseCode(String.valueOf(TransactionStatus.PENDING_MANUAL_REVERSAL.getStatusCode()));
            serviceAllocationRecord.setResponseMessage(e.getMessage());
            responseService.update(serviceAllocationRecord);
            paymentResponse = buildErrorResponse(e.getMessage(), serviceAllocationRecord.getTransaction());
        }
        paymentResponse.setSubscriberMsisdn(serviceAllocationRecord.getTransaction().getMsisdn1());
        return paymentResponse;
    }

    private TransactionResponse buildDummyResponse(Transaction transaction) {
        String message = "Token: 00000000000000000000\nMeter: 37131465793\nKwH: 47.2\nDebt: $0.00\nEnergy: $4.72\nREA: $0.28\nVAT: $0.00\nAmt: $amount\n".replace("37131465793", transaction.getAccount_number());
        message = message.replace("amount", String.valueOf(transaction.getAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN)));
        TransactionResponse response = new TransactionResponse();
        response.setField1("200");
        response.setField2(message);
        response.setField9("00000000000000000000");
        response.setField3("receipt");
        return response;
    }

    private PaymentResponse buildResponse(TransactionResponse creditResponse, ServiceAllocationRecord serviceResponse) {
        PaymentResponse response = new PaymentResponse(serviceResponse.getTransaction().getPaymentReference());
        response.setResponseMessage(serviceResponse.getResponseMessage());
        response.setResponseCode(Integer.valueOf(creditResponse.getField1()));
        response.setCallBackUrl(serviceResponse.getTransaction().getCallBackUrl());
        response.setTransactionStatus(serviceResponse.getTransactionStatus());
        response.setReceiptNumber(creditResponse.getField3());
        response.setSourceReference(serviceResponse.getTransaction().getSourceReference());
        response.setCustomerData(serviceResponse.getResponseData());
        return response;
    }

    private PaymentResponse buildErrorResponse(String message, Transaction transaction) {
        PaymentResponse paymentResponse = new PaymentResponse(transaction.getPaymentReference());
        paymentResponse.setTransactionStatus(TransactionStatus.FAILED);
        paymentResponse.setSourceReference(transaction.getSourceReference());
        paymentResponse.setCallBackUrl(transaction.getCallBackUrl());
        paymentResponse.setResponseCode(TransactionStatus.FAILED.getStatusCode());
        paymentResponse.setResponseMessage(message);
        return paymentResponse;
    }

    private void persistAirtimeResponse(Transaction transaction, ServiceAllocationRecord serviceResponse, PaymentResponse paymentResponse) {
        transactionDaoHelper.updateStatusFromServiceAllocationResponse(paymentResponse, transaction);
        responseService.save(serviceResponse);
    }

    private TransactionRequest buildZesaTokenRequest(Transaction transaction) {
        TransactionRequest request = new TransactionRequest();
        request.setField1(vendorCode);
        request.setField2(vendorKey);
        request.setField3(transaction.getMsisdn1());
        final ChecksumRequest checksumRequest = cpgHelper.buildChecksumRequest(zesaTxnType, transaction.getPaymentReference());
        final ChecksumResponse checksumResponse = CpgChecksumGenerator.getChecksum(checksumRequest);
        request.setField6(checksumResponse.getChecksum());
        request.setField7(zesaTxnType);
        request.setField8(applicationCode);
        request.setField10(transaction.getPaymentReference());
        request.setField11(transaction.getAccount_number());
        request.setField12(transaction.getAmount().toString());
        request.setField13(transaction.getCurrency().getCode());
        request.setField15(transaction.getCustomerName());
        return request;
    }

    public boolean isProductionProfile() {

        boolean isProductionProfile = false;

        for (String profileName : activeProfiles.split(",")) {
            log.info("Active profiles {}", profileName);
            if (profileName.equalsIgnoreCase("prod")) {
                isProductionProfile = true;
                break;
            }
        }
        return isProductionProfile;
    }
}
