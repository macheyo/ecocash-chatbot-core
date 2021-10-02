package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data;

import lombok.Builder;
import lombok.Data;
import zw.co.cassavasmartech.ecocashchatbotcore.model.UseCase;

@Builder
@Data
public class UnverifiedCustomerParameter {
    private UseCase usecase;
}
