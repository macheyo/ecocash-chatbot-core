package zw.co.cassavasmartech.ecocashchatbotcore.common.data;

import lombok.Data;
import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionType;

@Data
public class TransactionInvocationContext {

    private TransactionType transactionType;
    private CoreTransactionRequest request;

}
