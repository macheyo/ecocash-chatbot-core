package zw.co.cassavasmartech.ecocashchatbotcore.payment.cpo.enums;

public enum CpoTransactionStatus {

    PENDING("PENDING"),SUCCESS("SUCCESS"),FAILED("FAILED");

    private String status;

     CpoTransactionStatus(String status){
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

}
