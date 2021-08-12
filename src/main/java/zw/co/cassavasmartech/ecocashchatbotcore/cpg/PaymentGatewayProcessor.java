package zw.co.cassavasmartech.ecocashchatbotcore.cpg;

//import com.cassavasmartech.cpg.data.Response;
import zw.co.cassavasmartech.ecocashchatbotcore.cpg.data.*;
import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionResponse;


public interface PaymentGatewayProcessor {

    TransactionResponse merchantToSubscriber(MerchantToSubscriberRequest merchantToSubscriberRequest);

    TransactionResponse subscriberToMerchant(SubscriberToMerchantRequest subscriberToMerchantRequest);

    TransactionResponse merchantToMerchant(MerchantToMerchantRequest merchantToMerchantRequest);

    TransactionResponse pinReset(String msisdn);

    TransactionResponse lookupCustomer(String msisdn);

    TransactionResponse getStatement(String msisdn);

    TransactionResponse subscriberToSubscriber(SubscriberToSubscriberRequest subscriberToSubscriberRequest);

    TransactionResponse subscriberToBiller(SubscriberToBillerRequest subscriberToBillerRequest);
}
