package zw.co.cassavasmartech.ecocashchatbotcore.cpg;

import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionRequest;

public interface CheckSumGenerator {

    String generateCheckSum(TransactionRequest transactionRequest);
}
