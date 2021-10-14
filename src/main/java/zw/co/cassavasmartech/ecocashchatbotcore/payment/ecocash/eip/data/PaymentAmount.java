package zw.co.cassavasmartech.ecocashchatbotcore.payment.ecocash.eip.data;

import java.io.Serializable;

public class PaymentAmount implements Serializable {

    private ChargingInformation charginginformation;
    private ChargeMetaData chargeMetaData;


    public ChargingInformation getCharginginformation() {
        return charginginformation;
    }

    public void setCharginginformation(ChargingInformation charginginformation) {
        this.charginginformation = charginginformation;
    }

    public ChargeMetaData getChargeMetaData() {
        return chargeMetaData;
    }

    public void setChargeMetaData(ChargeMetaData chargeMetaData) {
        this.chargeMetaData = chargeMetaData;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PaymentAmount{");
        sb.append("charginginformation=").append(charginginformation);
        sb.append(", chargeMetaData=").append(chargeMetaData);
        sb.append('}');
        return sb.toString();
    }
}
