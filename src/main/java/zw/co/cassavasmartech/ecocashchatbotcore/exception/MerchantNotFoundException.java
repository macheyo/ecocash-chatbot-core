package zw.co.cassavasmartech.ecocashchatbotcore.exception;

public class MerchantNotFoundException extends RuntimeException{
    public MerchantNotFoundException(String id) {
        super("Could not find merchant with Id: " + id);
    }
}
