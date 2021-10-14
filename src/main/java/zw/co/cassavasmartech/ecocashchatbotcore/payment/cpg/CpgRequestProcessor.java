package zw.co.cassavasmartech.ecocashchatbotcore.payment.cpg;


import zw.co.cassavasmartech.esb.commons.data.Response;
import zw.co.cassavasmartech.esb.payment.cpg.data.MerchantToMerchantPaymentRequest;
import zw.co.cassavasmartech.esb.payment.cpg.data.MerchantToSubscriberPaymentRequest;
import zw.co.cassavasmartech.esb.payment.cpg.data.SubscriberToMerchantRequest;

public interface CpgRequestProcessor {

    Response customerLookUp(String msisdn) throws Exception;

    Response merchantToMerchantPayment(MerchantToMerchantPaymentRequest merchantToMerchantPaymentRequest) throws Exception;

    Response merchantToSubscriber(MerchantToSubscriberPaymentRequest merchantToSubscriberPaymentRequest) throws Exception;

    Response subscriberToMerchant(SubscriberToMerchantRequest subscriberToMerchantRequest) throws Exception;

}