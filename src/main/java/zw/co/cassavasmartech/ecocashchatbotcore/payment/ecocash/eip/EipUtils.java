package zw.co.cassavasmartech.ecocashchatbotcore.payment.ecocash.eip;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import zw.co.cassavasmartech.esb.commons.enums.ErrorResponse;
import zw.co.cassavasmartech.esb.commons.enums.TransactionStatus;
import zw.co.cassavasmartech.esb.payment.PaymentResponse;
import zw.co.cassavasmartech.esb.payment.ecocash.eip.data.AmountTransaction;

/**
 * Created by thomas on 8/20/18.
 */
@Slf4j
public class EipUtils {

    public static PaymentResponse buildPaymentResponse(final AmountTransaction eipResponse, final String paymentReference) {
        final PaymentResponse paymentResponse = new PaymentResponse(paymentReference);
        if (!ObjectUtils.isEmpty(eipResponse) && !StringUtils.isEmpty(eipResponse.getClientCorrelator())) {
            resolveSuccessMerchantResponse(paymentResponse, eipResponse);
            return paymentResponse;
        }
        log.warn("Error processing data request: {} ", eipResponse);
        resolveFailedPaymentResponse(paymentResponse, TransactionStatus.FAILED.getStatusCode(), eipResponse);
        return paymentResponse;
    }

    private static void resolveSuccessMerchantResponse(PaymentResponse merchantPaymentResponse, AmountTransaction responseAmountTransaction) {
        final TransactionStatus transactionStatus = resolveTransactionStatus(responseAmountTransaction.getTransactionOperationStatus());
        merchantPaymentResponse.setTransactionStatus(transactionStatus);
        merchantPaymentResponse.setReceiptNumber(responseAmountTransaction.getEcocashReference());
        merchantPaymentResponse.setResponseCode(transactionStatus.getStatusCode());
        merchantPaymentResponse.setResponseMessage(responseAmountTransaction.getTransactionOperationStatus());
    }

    private static void resolveFailedPaymentResponse(final PaymentResponse paymentResponse, int responseCode, AmountTransaction response) {
        paymentResponse.setTransactionStatus(TransactionStatus.FAILED);
        paymentResponse.setResponseCode(responseCode);
        String responseMsg;
        if(response!=null) responseMsg=response.getText();
        else responseMsg= ErrorResponse.NO_PAYMENT_API_RESPONSE.getMessage();
        paymentResponse.setResponseMessage(responseMsg);
    }

    private static TransactionStatus resolveTransactionStatus(final String eipStatus) {
        switch (eipStatus) {
            case "PENDING SUBSCRIBER VALIDATION":
                return TransactionStatus.PENDING_VALIDATION;
            case "COMPLETED":
                return TransactionStatus.SUCCESS;
            default:
                return TransactionStatus.FAILED;
        }
    }

}
