package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecursionParameter {
    private String intent;
    private int iteration;
}
