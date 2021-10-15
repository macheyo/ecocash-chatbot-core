package zw.co.cassavasmartech.ecocashchatbotcore.payment.cpg;


import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionResponse;
import zw.co.cassavasmartech.ecocashchatbotcore.payment.cpg.data.MerchantToMerchantPaymentRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.payment.cpg.data.MerchantToSubscriberPaymentRequest;
import zw.co.cassavasmartech.ecocashchatbotcore.payment.cpg.data.SubscriberToMerchantRequest;

public interface CpgRequestProcessor {

    TransactionResponse customerLookUp(String msisdn) throws Exception;

    TransactionResponse merchantToMerchantPayment(MerchantToMerchantPaymentRequest merchantToMerchantPaymentRequest) throws Exception;

    TransactionResponse merchantToSubscriber(MerchantToSubscriberPaymentRequest merchantToSubscriberPaymentRequest) throws Exception;

    TransactionResponse subscriberToMerchant(SubscriberToMerchantRequest subscriberToMerchantRequest) throws Exception;

}