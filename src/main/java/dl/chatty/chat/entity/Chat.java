package dl.chatty.chat.entity;

import lombok.Data;

@Data
public class Chat {
    private final String id;
    private final String title;
    private final String message;
    private final String createdBy;
}
