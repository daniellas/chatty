package dl.chatty.chat.protocol;

import java.util.Date;

import lombok.Data;

@Data
public class ChatMessage {
    private final String id;
    private final String from;
    private final String message;
    private final Date sentTs;
}
