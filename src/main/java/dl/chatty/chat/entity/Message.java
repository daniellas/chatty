package dl.chatty.chat.entity;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
@EqualsAndHashCode(of = { "id" })
@Data
public class Message {
    private final String id;
    private final String message;
    private final String from;
    private final Date sentTs;
}
