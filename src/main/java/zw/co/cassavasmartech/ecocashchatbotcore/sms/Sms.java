package zw.co.cassavasmartech.ecocashchatbotcore.sms;

import lombok.*;

@Data
@Builder
public class Sms{
    private String from;
    private String to;
    private String text;
}
