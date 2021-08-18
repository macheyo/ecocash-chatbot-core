package zw.co.cassavasmartech.ecocashchatbotcore.exception;

public class TicketNotFoundException extends RuntimeException{
    public TicketNotFoundException(String id) {
        super("Could not find ticket with Id: " + id);
    }
}
