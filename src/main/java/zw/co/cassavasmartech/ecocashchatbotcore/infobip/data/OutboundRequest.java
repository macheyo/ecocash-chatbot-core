package zw.co.cassavasmartech.ecocashchatbotcore.infobip.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OutboundRequest {
    private String conversationId;
    private String to;
    private String from;
    private Channel channel;
    private ContentType contentType;
    private Content content;
}
