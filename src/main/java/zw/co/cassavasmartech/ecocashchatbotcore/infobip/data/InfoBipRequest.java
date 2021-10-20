package zw.co.cassavasmartech.ecocashchatbotcore.infobip.data;


import lombok.Data;

@Data
public class InfoBipRequest {
    private String id;
    private SingleSendMessage singleSendMessage;
    private String conversationId;
    private Channel channel;
    private String from;
    private String to;
    private ContentType contentType;
    private Content content;
    private Direction direction;
    private String updatedAt;
}
