package zw.co.cassavasmartech.ecocashchatbotcore.payment.ecocash.eip.data;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MerchantPaymentRequest implements Serializable {

    private String msisdn;
    private String notifyUrl;
    private String merchantCode;
    private String merchantNumber;
    private String merchantPin;
    private BigDecimal amount;
    private String currency;
    private String sourceReference;
    private String remarks;


    public String getSourceReference() {
        return sourceReference;
    }

    public void setSourceReference(String sourceReference) {
        this.sourceReference = sourceReference;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getMerchantNumber() {
        return merchantNumber;
    }

    public void setMerchantNumber(String merchantNumber) {
        this.merchantNumber = merchantNumber;
    }

    public String getMerchantPin() {
        return merchantPin;
    }

    public void setMerchantPin(String merchantPin) {
        this.merchantPin = merchantPin;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MerchantPaymentRequest{");
        sb.append("notifyUrl='").append(notifyUrl).append('\'');
        sb.append(", sourceMerchantCode='").append(merchantCode).append('\'');
        sb.append(", merchantNumber='").append(merchantNumber).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
