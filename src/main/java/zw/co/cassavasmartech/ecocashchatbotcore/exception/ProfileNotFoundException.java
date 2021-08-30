package zw.co.cassavasmartech.ecocashchatbotcore.exception;

public class ProfileNotFoundException extends RuntimeException{
    public ProfileNotFoundException(String id) {
        super("Could not find profile with Id: " + id);
    }
}
