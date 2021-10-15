package zw.co.cassavasmartech.ecocashchatbotcore.payment.cpo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Transaction;
import zw.co.cassavasmartech.ecocashchatbotcore.payment.cpo.data.CardPaymentResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.payment.cpo.enums.CpoTransactionStatus;


@Service
@Slf4j
public class CpoHelper {

    public boolean wasPaymentCompleted(String status) {
        if (CpoTransactionStatus.SUCCESS.getStatus().equalsIgnoreCase(status))
            return true;
        else if (CpoTransactionStatus.FAILED.getStatus().equalsIgnoreCase(status))
            return true;
        else
            return false;
    }

    public boolean isSameTransaction(Transaction transaction, CardPaymentResponse cardPaymentResponse) {
        boolean result = transaction.getAmount().compareTo(cardPaymentResponse.getAmount()) == 0
                &&
                (transaction.getCurrency().getCode().equalsIgnoreCase(cardPaymentResponse.getCurrency()) ||
                        transaction.getCurrency().getStandardCode().equalsIgnoreCase(cardPaymentResponse.getCurrency()))
                &&
                transaction.getSourceReference().equalsIgnoreCase(cardPaymentResponse.getOrderNumber());
        return result;
    }

}
