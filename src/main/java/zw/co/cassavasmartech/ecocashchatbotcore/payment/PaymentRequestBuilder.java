package zw.co.cassavasmartech.ecocashchatbotcore.payment;

import zw.co.cassavasmartech.esb.commons.enums.RequestChannel;
import zw.co.cassavasmartech.esb.model.Currency;
import zw.co.cassavasmartech.esb.model.PaymentMethod;

import java.math.BigDecimal;

public class PaymentRequestBuilder {
    private final PaymentRequest paymentRequest;

    public PaymentRequestBuilder() {
        paymentRequest = new PaymentRequest();
    }

    public PaymentRequestBuilder msisdn(String msisdn) {
        this.paymentRequest.setSponsoringMsisdn(msisdn);
        return this;
    }

    public PaymentRequestBuilder remarks(String remarks) {
        this.paymentRequest.setRemarks(remarks);
        return this;
    }

    public PaymentRequestBuilder amount(BigDecimal amount) {
        paymentRequest.setAmount(amount);
        return this;
    }

    public PaymentRequestBuilder requestChannel(RequestChannel requestChannel) {
        paymentRequest.setRequestChannel(requestChannel);
        return this;
    }

    public PaymentRequestBuilder paymentMethod(PaymentMethod paymentMethod) {
        paymentRequest.setPaymentMethod(paymentMethod);
        return this;
    }

    public PaymentRequestBuilder currency(Currency currency) {
        paymentRequest.setCurrency(currency);
        return this;
    }

    public PaymentRequest build() {
        return paymentRequest;
    }
}