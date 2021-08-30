package zw.co.cassavasmartech.ecocashchatbotcore.eip;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.EipTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.SubscriberToMerchant;

public interface EipService {
    EipTransaction postPayment(SubscriberToMerchant subscriberToMerchant);

    Boolean handleCallback(EipTransaction response);

}
