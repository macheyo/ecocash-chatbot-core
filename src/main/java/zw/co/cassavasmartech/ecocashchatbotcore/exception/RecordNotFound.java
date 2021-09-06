package zw.co.cassavasmartech.ecocashchatbotcore.exception;

public class RecordNotFound extends RuntimeException {


    public RecordNotFound(String message) {
        super(message);
    }

    public RecordNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
