package zw.co.cassavasmartech.ecocashchatbotcore.sms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import zw.co.cassavasmartech.ecocashchatbotcore.notification.Notification;


@ToString
@Getter
@Setter
@NoArgsConstructor
public class Sms extends Notification {

    private long id;
    private String from;
    private String to;
    private String text;

    public Sms(String to, String text) {
        this.to = to;
        this.text = text;
    }
}
