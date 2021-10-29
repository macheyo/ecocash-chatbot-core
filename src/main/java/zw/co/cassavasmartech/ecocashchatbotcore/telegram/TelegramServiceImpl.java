package zw.co.cassavasmartech.ecocashchatbotcore.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@Service
public class TelegramServiceImpl implements TelegramService {
    @Autowired
    TelegramConfigurationProperties telegramConfigurationProperties;
    @Override
    public void sendMessage(String chatId, String text) {
        String urlString = String.format(telegramConfigurationProperties.getSendMessageUrl(), telegramConfigurationProperties.getToken(), chatId, text);
        try{
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void sendDocument(String chatId, String documentURL) {
        String urlString = String.format(telegramConfigurationProperties.getSendDocumentUrl(), telegramConfigurationProperties.getToken(), chatId, documentURL,"This is your statement. Can I do anything else for you?");
        try{
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
