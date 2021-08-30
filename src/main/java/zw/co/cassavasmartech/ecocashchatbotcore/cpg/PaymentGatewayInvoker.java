package zw.co.cassavasmartech.ecocashchatbotcore.cpg;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PostTransaction;
import zw.co.cassavasmartech.ecocashchatbotcore.model.PostTransactionResponse;

import java.util.Optional;

public interface PaymentGatewayInvoker {
    Optional<PostTransactionResponse> invoke(PostTransaction postTransaction);
    public Optional<PostTransactionResponse> invoke2(PostTransaction postTransaction);
}
