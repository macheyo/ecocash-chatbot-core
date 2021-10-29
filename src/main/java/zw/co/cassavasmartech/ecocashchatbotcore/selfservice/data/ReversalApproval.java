package zw.co.cassavasmartech.ecocashchatbotcore.selfservice.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReversalApproval {

    private Long reversalId;
    private BandType bandType;
    private Boolean applyCharge;
}
