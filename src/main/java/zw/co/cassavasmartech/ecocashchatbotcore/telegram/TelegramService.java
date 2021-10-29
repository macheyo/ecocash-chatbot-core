package zw.co.cassavasmartech.ecocashchatbotcore.telegram;

import org.springframework.stereotype.Service;

@Service
public interface TelegramService {
    void sendMessage(String chatId, String message);

    void sendDocument(String chatId, String documentURL);
}
