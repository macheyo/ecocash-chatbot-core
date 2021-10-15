package zw.co.cassavasmartech.ecocashchatbotcore.common;


import zw.co.cassavasmartech.ecocashchatbotcore.common.data.cpg.ChecksumRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionResponse;

public interface CpgHelper {

    ChecksumRequest buildChecksumRequest(String transactionType, String reference)  ;

    TransactionResponse invokeApi(TransactionRequest request, String url) throws Exception;
}

