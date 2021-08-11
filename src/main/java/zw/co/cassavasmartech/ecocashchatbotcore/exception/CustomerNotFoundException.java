package zw.co.cassavasmartech.ecocashchatbotcore.exception;

public class CustomerNotFoundException extends RuntimeException{
    public CustomerNotFoundException(String id) {
        super("Could not find customer with Id: " + id);
    }
}
