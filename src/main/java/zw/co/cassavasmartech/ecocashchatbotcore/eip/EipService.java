package zw.co.cassavasmartech.ecocashchatbotcore.eip;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.EipTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.SubscriberToMerchantRequest;

public interface EipService {
    EipTransaction postPayment(SubscriberToMerchantRequest subscriberToMerchantRequest);

    Boolean handleCallback(EipTransaction response);

}
