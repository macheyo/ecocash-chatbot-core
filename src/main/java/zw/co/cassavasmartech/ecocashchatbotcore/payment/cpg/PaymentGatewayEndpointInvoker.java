package zw.co.cassavasmartech.ecocashchatbotcore.payment.cpg;

import zw.co.cassavasmartech.esb.commons.data.PostTransaction;
import zw.co.cassavasmartech.esb.commons.data.PostTransactionResponse;
import zw.co.cassavasmartech.esb.exception.BusinessRuntimeException;

public interface PaymentGatewayEndpointInvoker {
    PostTransactionResponse invoke(PostTransaction request, String endpointUrl) throws BusinessRuntimeException;
}
