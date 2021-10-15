package zw.co.cassavasmartech.ecocashchatbotcore.payment.ecocash.eip.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class AmountTransactionRefund extends AmountTransaction {
    private String originalServerReferenceCode;
    private String originalEcocashReference;
    private String remark;
    @JsonIgnore
    private String remarks;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AmountTransactionRefund{");
        sb.append(super.toString()).append("\n");
        sb.append(", originalEcocashReference='").append(originalEcocashReference).append('\'');
        sb.append(", remark='").append(remark).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
