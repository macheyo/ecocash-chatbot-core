package zw.co.cassavasmartech.ecocashchatbotcore.selfservice.data;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EcocashTransaction {

    private String transactionReference;
    private LocalDateTime transactionDate;
    private String senderMobileNumber;
    private String recipientMobileNumber;
    private BigDecimal amount;
    private boolean isReversed;
}
