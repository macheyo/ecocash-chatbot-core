package zw.co.cassavasmartech.ecocashchatbotcore.eip;

import zw.co.cassavasmartech.ecocashchatbotcore.eip.data.EipTransaction;

import java.util.Optional;

public interface EipInvoker {
    Optional<EipTransaction> invoke(EipTransaction eipTransaction);
}
