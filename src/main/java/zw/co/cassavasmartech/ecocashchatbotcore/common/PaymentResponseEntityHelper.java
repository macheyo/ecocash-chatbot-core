package zw.co.cassavasmartech.ecocashchatbotcore.common;

import org.springframework.stereotype.Service;
import zw.co.cassavasmartech.ecocashchatbotcore.model.ServiceAllocationRecord;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Transaction;
import zw.co.cassavasmartech.ecocashchatbotcore.payment.cpo.data.CardPaymentResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.payment.ecocash.eip.data.AmountTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.payment.ecocash.eip.data.AmountTransactionRefund;


@Service
public class PaymentResponseEntityHelper {


    public ServiceAllocationRecord buildPaymentResponseEntity(AmountTransaction response, Transaction transaction){
        ServiceAllocationRecord serviceResponse=new ServiceAllocationRecord();
        return serviceResponse;
    }

    public ServiceAllocationRecord buildPaymentResponseEntity(AmountTransactionRefund response, Transaction transaction){
        ServiceAllocationRecord serviceResponse=new ServiceAllocationRecord();
        return serviceResponse;
    }

    public ServiceAllocationRecord buildPaymentResponseEntity(CardPaymentResponse response, Transaction transaction){
        ServiceAllocationRecord serviceResponse=new ServiceAllocationRecord();
        return serviceResponse;
    }
}
