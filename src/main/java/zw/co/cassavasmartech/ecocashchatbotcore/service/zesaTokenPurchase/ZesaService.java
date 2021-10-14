package zw.co.cassavasmartech.ecocashchatbotcore.service.zesaTokenPurchase;

import zw.co.cassavasmartech.ecocashchatbotcore.model.ServiceAllocationRecord;
import zw.co.cassavasmartech.ecocashchatbotcore.payment.PaymentResponse;

public interface ZesaService {

    PaymentResponse postPayment(ServiceAllocationRecord topUpRequest);

}
