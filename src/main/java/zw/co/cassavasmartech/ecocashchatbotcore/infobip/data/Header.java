package zw.co.cassavasmartech.ecocashchatbotcore.infobip.data;

import lombok.Data;

@Data
public class Header {
    private Type type;
    private String url;
    private String filename;
}
