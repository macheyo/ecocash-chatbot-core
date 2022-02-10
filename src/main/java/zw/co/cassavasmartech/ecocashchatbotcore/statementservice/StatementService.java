package zw.co.cassavasmartech.ecocashchatbotcore.statementservice;

import org.springframework.http.ResponseEntity;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Statement;
import zw.co.cassavasmartech.ecocashchatbotcore.model.StatementRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface StatementService {
    public Statement getStatement(StatementRequest statementRequest);

    ResponseEntity<byte[]> getStatementFile(String documentId, HttpServletRequest req, HttpServletResponse resp);
}
