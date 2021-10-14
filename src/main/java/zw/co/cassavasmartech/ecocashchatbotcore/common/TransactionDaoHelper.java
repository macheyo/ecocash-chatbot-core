package zw.co.cassavasmartech.ecocashchatbotcore.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.common.enums.TransactionStatus;
import zw.co.cassavasmartech.ecocashchatbotcore.model.ServiceAllocationRecord;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Transaction;
import zw.co.cassavasmartech.ecocashchatbotcore.payment.PaymentResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.transaction.TransactionService;

import java.math.BigDecimal;

@Service
public class TransactionDaoHelper {

    @Autowired
    private TransactionService transactionService;

    public void updateStatusFromServiceAllocationResponse(PaymentResponse response, Transaction transaction) {
        if (response.getTransactionStatus() == TransactionStatus.SUCCESS) {
            transaction.setServiceCredited(true);
            transaction.setTranStatus(TransactionStatus.SUCCESS);
        } else if (response.getTransactionStatus() == TransactionStatus.PENDING_MANUAL_REVERSAL) {
            transaction.setServiceCredited(false);
            transaction.setTranStatus(TransactionStatus.PENDING_MANUAL_REVERSAL);
        } else {
            transaction.setServiceCredited(false);
            transaction.setTranStatus(TransactionStatus.PENDING_REVERSAL);
        }
        transactionService.update(transaction);
    }

//    public void updateAmount(ServiceAllocationRecord serviceAllocationRecord, Transaction transaction) {
//        BigDecimal value3 = serviceAllocationRecord.getTransaction().getCommision().setScale(2, BigDecimal.ROUND_HALF_EVEN);
//        BigDecimal value = serviceAllocationRecord.getTransaction().getAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN).subtract(value3);
//        transaction.setAmount(value);
//    }
}
