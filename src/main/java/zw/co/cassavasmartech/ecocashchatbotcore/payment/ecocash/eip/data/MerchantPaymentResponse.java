package zw.co.cassavasmartech.ecocashchatbotcore.payment.ecocash.eip.data;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MerchantPaymentResponse {

    private boolean successful;
    private String responseMessage;
    private String referenceNumber;
    private String statusCode;
    private String transactionOperationStatus;

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getTransactionOperationStatus() {
        return transactionOperationStatus;
    }

    public void setTransactionOperationStatus(String transactionOperationStatus) {
        this.transactionOperationStatus = transactionOperationStatus;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MerchantPaymentResponse{");
        sb.append("successful=").append(successful);
        sb.append(", responseMessage='").append(responseMessage).append('\'');
        sb.append(", referenceNumber='").append(referenceNumber).append('\'');
        sb.append(", statusCode='").append(statusCode).append('\'');
        sb.append(", transactionOperationStatus='").append(transactionOperationStatus).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
