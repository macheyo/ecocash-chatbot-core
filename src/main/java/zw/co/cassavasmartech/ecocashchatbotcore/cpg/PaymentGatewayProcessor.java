package zw.co.cassavasmartech.ecocashchatbotcore.cpg;

//import com.cassavasmartech.cpg.data.Response;

import zw.co.cassavasmartech.ecocashchatbotcore.cpg.data.*;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PostTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.model.Registration;
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

    TransactionResponse validateCustomer(ValidateCustomerRequest validateCustomerRequest);

    TransactionResponse lookupBiller(BillerLookupRequest billerLookupRequest);

    TransactionResponse subscriberAirtime(SubscriberAirtimeRequest subscriberAirtimeRequest);

    TransactionResponse registerCustomer(Registration registration);

    Boolean handleCallBack(PostTransaction postTransaction);

    TransactionResponse lookupMerchant(MerchantLookupRequest merchantLookupRequest);
}
