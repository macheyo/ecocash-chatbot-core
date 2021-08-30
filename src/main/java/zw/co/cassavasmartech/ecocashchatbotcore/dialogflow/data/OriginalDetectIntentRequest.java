package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data;

import lombok.Data;

@Data
public class OriginalDetectIntentRequest {
    private String source;
    private String version;
    private Object payload;
}
