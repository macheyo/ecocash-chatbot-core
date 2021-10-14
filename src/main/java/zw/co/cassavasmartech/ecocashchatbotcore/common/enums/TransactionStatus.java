package zw.co.cassavasmartech.ecocashchatbotcore.common.enums;

import java.util.Arrays;

public enum TransactionStatus {
    SUCCESS(200), FAILED(500), REVERSAL(600), PENDING_REVERSAL(604), PENDING(601), REPOSTED(602), TIMEOUT(799), PENDING_VALIDATION(603),
    PENDING_MANUAL_REVERSAL(605),REVERSED(606),PAID(607),PENDING_TOKEN_CHECK(608);

    final int statusCode;

    TransactionStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public static TransactionStatus fromStatusCode(int statusCode) {
        return Arrays.stream(TransactionStatus.values()).filter(t -> t.statusCode == statusCode).findFirst().orElse(null);
    }
}
