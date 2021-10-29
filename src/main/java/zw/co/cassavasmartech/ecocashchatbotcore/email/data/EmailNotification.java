package zw.co.cassavasmartech.ecocashchatbotcore.email.data;

import lombok.Data;

@Data
public class EmailNotification {

    private String sender;
    private String subject;
    private String templateName;
    private String to;
    private String filePath;

}
