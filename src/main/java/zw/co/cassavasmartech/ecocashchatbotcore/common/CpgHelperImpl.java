package zw.co.cassavasmartech.ecocashchatbotcore.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import zw.co.cassavasmartech.ecocashchatbotcore.common.data.cpg.ChecksumRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionResponse;


import java.net.URI;

@Service
@Slf4j
public class CpgHelperImpl implements CpgHelper {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CallbackInvoker callbackInvoker;
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

    public ChecksumRequest buildChecksumRequest(String transactionType, String reference) {
        ChecksumRequest request = new ChecksumRequest();
        request.setKeyStoreAlias(keystoreAlias);
        request.setKeyStoreLocation(keystoreLocation);
        request.setKeystorePass(keystorePass);
        request.setTransactionType(transactionType);
        request.setVendorCode(vendorCode);
        request.setVendorKey(vendorKey);
        request.setTransactionReference(reference);
        request.setApplicationCode(applicationCode);
        request.setKeystoreType(keyStoreType);
        return request;
    }

    public TransactionResponse invokeApi(TransactionRequest request, String url) throws Exception {
        final RequestEntity<TransactionRequest> requestEntity = new RequestEntity<>(request,
                callbackInvoker.httpHeaders(), HttpMethod.POST, new URI(url));
        log.info("\n\nCpg Request for txn {} is : {}", request.getField10(), requestEntity);
        final ResponseEntity<TransactionResponse> responseEntity = restTemplate.exchange(requestEntity, TransactionResponse.class);
        log.info("\n\nCpg Response for txn {} is : {}", request.getField10(),responseEntity);
        return responseEntity.getBody();
    }
}

