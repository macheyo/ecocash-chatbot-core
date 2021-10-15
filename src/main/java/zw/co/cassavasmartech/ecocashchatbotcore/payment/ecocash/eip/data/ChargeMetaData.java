package zw.co.cassavasmartech.ecocashchatbotcore.payment.ecocash.eip.data;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement()
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChargeMetaData implements Serializable {

    private String channel;
    private String purchaseCategoryCode;
    private String onBeHalfOf;
    private String serviceId;

    public ChargeMetaData(String channel) {
        this.channel = channel;
    }

    public ChargeMetaData() {
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPurchaseCategoryCode() {
        return purchaseCategoryCode;
    }

    public void setPurchaseCategoryCode(String purchaseCategoryCode) {
        this.purchaseCategoryCode = purchaseCategoryCode;
    }

    public String getOnBeHalfOf() {
        return onBeHalfOf;
    }

    public void setOnBeHalfOf(String onBeHalfOf) {
        this.onBeHalfOf = onBeHalfOf;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ChargeMetaData{");
        sb.append("channel='").append(channel).append('\'');
        sb.append(", purchaseCategoryCode='").append(purchaseCategoryCode).append('\'');
        sb.append(", onBeHalfOf='").append(onBeHalfOf).append('\'');
        sb.append(", serviceId='").append(serviceId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
