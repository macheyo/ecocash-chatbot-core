package zw.co.cassavasmartech.ecocashchatbotcore.email.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailNotification {

    private String sender;
    private String subject;
    private String body;
    private String to;
    private String filePath;

}
