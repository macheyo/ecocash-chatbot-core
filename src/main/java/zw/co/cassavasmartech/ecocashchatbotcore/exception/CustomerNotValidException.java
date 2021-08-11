package zw.co.cassavasmartech.ecocashchatbotcore.exception;

public class CustomerNotValidException extends RuntimeException {
    public CustomerNotValidException(String id) {
        super("Customer with MSISDN: " + id +" is not a valid Ecocash subscriber");
    }
}
