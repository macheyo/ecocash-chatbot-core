package zw.co.cassavasmartech.ecocashchatbotcore.payment.cpg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zw.co.cassavasmartech.esb.commons.data.TransactionRequest;

public class RequestBuilder {

    final private static Logger logger = LoggerFactory.getLogger(RequestBuilder.class);
    TransactionRequest request;

    ChecksumGenerator checksumGenerator;

    public RequestBuilder() {
        request = new TransactionRequest();
    }

    public RequestBuilder(TransactionRequest request) {
        this.request = request;
    }

    public RequestBuilder vendorApiKey(String vendorApiKey) {
        request.setField2(vendorApiKey);
        return this;
    }


    public RequestBuilder vendorCode(String vendorCode) {
        request.setField1(vendorCode);
        return this;
    }

    public RequestBuilder reference(String reference) {
        request.setField10(reference);
        return this;
    }

    public RequestBuilder msisdn2(String msisdn2) {
        request.setField11(msisdn2);
        return this;
    }

    public RequestBuilder tranType(String tranType) {
        request.setField7(tranType);
        return this;
    }

    public RequestBuilder merchantCode(String merchantCode) {
        request.setField9(merchantCode);
        return this;
    }


    public RequestBuilder paymentMethod(String paymentMethod) {
        request.setField9(paymentMethod);
        return this;
    }

    public RequestBuilder countryCode(String accountNumber) {
        request.setField23(accountNumber);
        return this;
    }

    public RequestBuilder accountNumber(String accountNumber) {
        request.setField4(accountNumber);
        return this;
    }

    public RequestBuilder msisdn(String msisdn) {
        request.setField3(msisdn);
        return this;
    }


    public RequestBuilder amount(String amount) {
        request.setField12(amount);
        return this;
    }


    public RequestBuilder currency(String currency) {
        request.setField13(currency);
        return this;
    }


    public RequestBuilder checksumGenerator(ChecksumGenerator checksumGenerator) {
        this.checksumGenerator = checksumGenerator;
        return this;
    }

    public TransactionRequest build() throws Exception {
        logger.trace("request: {}", request);
        final String checksum = checksumGenerator.generateCheckSum(request);
        request.setField6(checksum);
        return request;
    }

    public RequestBuilder applicationCode(String serviceName) {
        request.setField8(serviceName);
        return this;
    }


    public RequestBuilder pin(String pin) {
        request.setField5(pin);
        return this;
    }

}
