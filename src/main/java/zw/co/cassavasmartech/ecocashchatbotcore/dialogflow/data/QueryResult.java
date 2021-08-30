package zw.co.cassavasmartech.ecocashchatbotcore.dialogflow.data;

import lombok.Data;

import java.util.List;

@Data
public class QueryResult {
    private String queryText;
    private String action;
    private Object parameters;
    private Boolean allRequiredParamsPresent;
    private List<OutputContext> outputContexts;
    private Intent intent;
    private Double intentDetectionConfidence;
    private String languageCode;
    private SentimentAnalysisResult sentimentAnalysisResult;
}
