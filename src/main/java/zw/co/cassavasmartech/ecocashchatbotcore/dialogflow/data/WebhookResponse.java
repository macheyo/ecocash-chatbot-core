package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WebhookResponse {
    private String fulfillmentText;
    private String source;
    private Object[] outputContexts;
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
