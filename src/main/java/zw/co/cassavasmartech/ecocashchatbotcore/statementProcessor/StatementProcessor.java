package zw.co.cassavasmartech.ecocashchatbotcore.statementProcessor;

import zw.co.cassavasmartech.ecocashchatbotcore.model.Answer;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Statement;
import zw.co.cassavasmartech.ecocashchatbotcore.model.StatementRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.model.SubscriberDto;

import java.util.List;

public interface StatementProcessor {
    public Statement getStatement(StatementRequest statementRequest);

}
