package zw.co.cassavasmartech.ecocashchatbotcore.infobip.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Content {
    private String url;
    private String text;
    private String context;
    private Type type;
    private String showUrlPreview;
    private String templateName;
    private Language language;
    private Header header;
    private Body body;
    private Buttons[] buttons;
}
