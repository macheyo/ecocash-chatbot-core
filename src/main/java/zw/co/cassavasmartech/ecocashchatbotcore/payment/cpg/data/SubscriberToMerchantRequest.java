package zw.co.cassavasmartech.ecocashchatbotcore.payment.cpg.data;

import java.math.BigDecimal;

/**
 * Created by thomas on 8/21/18.
 */
public class SubscriberToMerchantRequest {
    private String currency;
    private BigDecimal amount;
    private String merchantMsisdn;
    private String subscriberMsisdn;
    private String pin;
    private String sourceRef;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMerchantMsisdn() {
        return merchantMsisdn;
    }

    public void setMerchantMsisdn(String merchantMsisdn) {
        this.merchantMsisdn = merchantMsisdn;
    }

    public String getSubscriberMsisdn() {
        return subscriberMsisdn;
    }

    public void setSubscriberMsisdn(String subscriberMsisdn) {
        this.subscriberMsisdn = subscriberMsisdn;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getSourceRef() {
        return sourceRef;
    }

    public void setSourceRef(String sourceRef) {
        this.sourceRef = sourceRef;
    }

    @Override
    public String toString() {
        return "SubscriberToMerchantRequest{" +
                "currency='" + currency + '\'' +
                ", amount=" + amount +
                ", merchantMsisdn='" + merchantMsisdn + '\'' +
                ", subscriberMsisdn='" + subscriberMsisdn + '\'' +
                ", pin='" + pin + '\'' +
                ", sourceRef='" + sourceRef + '\'' +
                '}';
    }
}
