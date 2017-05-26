package dl.chatty.chat;

import lombok.Data;

@Data
public class Chat {
    private final String id;
    private final String title;
    private final String message;
    private final User createdBy;
}
