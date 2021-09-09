package zw.co.cassavasmartech.ecocashchatbotcore.selfServiceCore.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ReversalDto {
    private Long id;
    private String failureReason;
    private String reference;
    private ReversalAuthStatus reversalAuthStatus;
    private String originalSenderMobileNumber;
    private String originalRecipientMobileNumber;
    private BigDecimal amount;
    private ReversalStatus status;
    private LocalDateTime approvedDate;
    private LocalDateTime cancelledDate;
    private LocalDateTime failedDate;
    private String currency;
    private TransactionType transactionType;
    private BandType bandType;
    private String subscriberToMerchantRef;
    private String merchantToSubscriberRef;
    private LocalDateTime processingDate;
    private BigDecimal processedAmount;
    private BigDecimal amountCharged;
    private String adminFeeExemptionReason;
}
