package dl.chatty.chat.entity;

import java.util.Date;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
@Data
public class Message {
    private final String message;
    private final String from;
    private final Date createTs;
}
