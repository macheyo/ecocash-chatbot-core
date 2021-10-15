package zw.co.cassavasmartech.ecocashchatbotcore.payment.cpg;

import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionRequest;

import java.util.Date;

/**
 * Created by thomas on 2/10/18.
 */
public interface ChecksumGenerator {
    String generateReference(String msisdn, Date requestTimeStamp);

    String generateCheckSum(TransactionRequest request) throws Exception;
}
