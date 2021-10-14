package zw.co.cassavasmartech.ecocashchatbotcore.payment.ecocash.eip.data;

public enum TransactionOperationStatus {
    CHARGED("CHARGED"),
    FAILED("FAILED"),
    COMPLETED("COMPLETED"),
    PROCESSING_CHARGE("PROCESSING_CHARGE"),
    PENDING_SUBSCRIBER_VALIDATION("PENDING SUBSCRIBER VALIDATION");
    private String value;

    TransactionOperationStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
