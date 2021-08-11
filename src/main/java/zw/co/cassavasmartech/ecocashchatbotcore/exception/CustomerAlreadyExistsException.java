package zw.co.cassavasmartech.ecocashchatbotcore.exception;

public class CustomerAlreadyExistsException extends RuntimeException{
    public CustomerAlreadyExistsException(String id) {
        super("Customer with ID: " +id+" already exists");
    }
}
