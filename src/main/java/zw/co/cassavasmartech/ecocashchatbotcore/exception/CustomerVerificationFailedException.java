package zw.co.cassavasmartech.ecocashchatbotcore.exception;

public class CustomerVerificationFailedException extends RuntimeException{
    public CustomerVerificationFailedException() {
        super("expired");
    }
}
