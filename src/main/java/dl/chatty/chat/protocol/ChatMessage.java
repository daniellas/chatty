package dl.chatty.chat.protocol;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
@EqualsAndHashCode(of = { "id" })
@Data
public class ChatMessage {
    private final String id;
    private final String from;
    @NotNull
    private final String message;
    private final Date sentTs;
}
