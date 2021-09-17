package zw.co.cassavasmartech.ecocashchatbotcore.exception;

public class PromptNotFoundException extends RuntimeException{
    public PromptNotFoundException(String id) {
        super("Could not find prompt with Id: " + id);
    }
}
