package dl.chatty.chat.entity;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
@EqualsAndHashCode(of = { "id" })
@Data
public class Chat {
    private final String id;
    private final String title;
    private final String createdBy;
    private final Date createTs;
}
