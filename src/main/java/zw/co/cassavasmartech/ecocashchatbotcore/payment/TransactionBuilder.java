package zw.co.cassavasmartech.ecocashchatbotcore.payment;

import zw.co.cassavasmartech.esb.commons.enums.RequestChannel;
import zw.co.cassavasmartech.esb.commons.enums.TransactionStatus;
import zw.co.cassavasmartech.esb.model.*;

import java.math.BigDecimal;

public class TransactionBuilder {
    private final Transaction transaction;

    public TransactionBuilder() {
        transaction = new Transaction();
    }

    public TransactionBuilder tranStatus(TransactionStatus transactionStatus) {
        transaction.setTranStatus(transactionStatus);
        return this;
    }

    public TransactionBuilder paymentMethod(PaymentMethod paymentMethod) {
        transaction.setPaymentMethod(paymentMethod);
        return this;
    }

    public TransactionBuilder paymentReference(String paymentReference) {
        transaction.setPaymentReference(paymentReference);
        return this;
    }

    public TransactionBuilder partner(Partner partner) {
        transaction.setPartner(partner);
        return this;
    }

    public TransactionBuilder amount(BigDecimal amount) {
        transaction.setAmount(amount);
        return this;
    }

    public TransactionBuilder receiptNumber(String receiptNumber) {
        transaction.setReceiptNumber(receiptNumber);
        return this;
    }

    public TransactionBuilder channel(RequestChannel channel) {
        transaction.setChannel(channel);
        return this;
    }


    public TransactionBuilder currency(Currency currency) {
        transaction.setCurrency(currency);
        return this;
    }

    public TransactionBuilder accountNumber(String accountNumber) {
        transaction.setAccount_number(accountNumber);
        return this;
    }

    public TransactionBuilder remarks(String remarks) {
        transaction.setRemarks(remarks);
        return this;
    }

    public TransactionBuilder sourceReference(String sourceReference) {
        transaction.setSourceReference(sourceReference);
        return this;
    }

    public TransactionBuilder msisdn1(String msisdn1) {
        transaction.setMsisdn1(msisdn1);
        return this;
    }

    public TransactionBuilder msisdn2(String msisdn2) {
        transaction.setMsisdn2(msisdn2);
        return this;
    }

    public TransactionBuilder merchantMsisdn1(String merchantMsisdn1) {
        transaction.setMerchantMsisdn1(merchantMsisdn1);
        return this;
    }

    public TransactionBuilder callBackUrl(String callbackUrl) {
        transaction.setCallBackUrl(callbackUrl);
        return this;
    }

    public TransactionBuilder process(ProcessRegister process) {
        transaction.setProcessRegister(process);
        return this;
    }

    public Transaction build() {
        return this.transaction;
    }

    public TransactionBuilder customerName(String customerName) {
        this.transaction.setCustomerName(customerName);
        return this;
    }

    public TransactionBuilder taxCode(String taxCode) {
        this.transaction.setTaxCode(taxCode);
        return this;
    }

    public TransactionBuilder zimraOfficeCode(String zimraOfficeCode) {
        this.transaction.setZimraOfficeCode(zimraOfficeCode);
        return this;
    }

    public TransactionBuilder customerEmailAddress(String customerEmailAddress) {
        this.transaction.setCustomerEmailAddress(customerEmailAddress);
        return this;
    }

    public TransactionBuilder customerData(String customerData) {
        this.transaction.setCustomerData(customerData);
        return this;
    }

    public TransactionBuilder commision(BigDecimal commision) {
        this.transaction.setCommision(commision);
        return this;
    }



    public TransactionBuilder productId(Integer productId) {
        this.transaction.setProductId(productId);
        return this;
    }

    public TransactionBuilder zolServiceLogin(String zolServiceLogin) {
        this.transaction.setZolServiceLogin(zolServiceLogin);
        return this;
    }


    public TransactionBuilder zolServiceName(String zolServiceName) {
        this.transaction.setZolServiceName(zolServiceName);
        return this;
    }

    public TransactionBuilder zolServicePassword(String zolServicePassword) {
        this.transaction.setZolServicePassword(zolServicePassword);
        return this;
    }


}
