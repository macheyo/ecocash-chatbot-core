package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;

@Data
public class WebhookRequest {
    private String responseId;
    private QueryResult queryResult;
    private OriginalDetectIntentRequest originalDetectIntentRequest;
    private String session;
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

}
