package zw.co.cassavasmartech.ecocashchatbotcore.email;


import zw.co.cassavasmartech.ecocashchatbotcore.email.data.EmailNotification;

public interface EmailService {
    void send(EmailNotification emailNotification);
}
