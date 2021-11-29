package zw.co.cassavasmartech.ecocashchatbotcore.cpg.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidateCustomerRequest {
    private String msisdn;
    private String pin;
}
