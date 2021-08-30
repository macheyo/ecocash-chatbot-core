package zw.co.cassavasmartech.ecocashchatbotcore.eip.data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import zw.co.cassavasmartech.ecocashchatbotcore.model.BaseEntity;

import javax.persistence.*;

@Entity
@Data
public class Merchant extends BaseEntity {
    private String name;
    private String companyReference;
    private String transactionType;
    private String serviceId;
    private String merchantCode;
    private String merchantPin;
    private String merchantNumber;
    private String location;
    private String remarks;

}
