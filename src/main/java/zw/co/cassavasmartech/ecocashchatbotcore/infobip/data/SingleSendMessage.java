package zw.co.cassavasmartech.ecocashchatbotcore.infobip.data;

import lombok.Data;

@Data
public class SingleSendMessage {
    private Phone from;
    private Phone to;
    private Content content;
    private Contact contact;
    private Channel channel;
    private Direction direction;
}
