package zw.co.cassavasmartech.ecocashchatbotcore.notification;


import zw.co.cassavasmartech.ecocashchatbotcore.model.Transaction;

public interface NotificationProcessor {

    void sendNotification(Transaction transaction);
}
