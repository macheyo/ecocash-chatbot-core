package zw.co.cassavasmartech.ecocashchatbotcore.tariffsService;

import zw.co.cassavasmartech.ecocashchatbotcore.model.TransactionResponse;

import java.math.BigDecimal;

public interface TariffServiceProcessor {
    public TransactionResponse tariffRegistered(BigDecimal amount);
}
