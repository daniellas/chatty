package dl.chatty.chat.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of", onConstructor = @__({ @JsonCreator }))
@Data
public class Chat {
    private final String id;
    private final String title;
    private final String createdBy;
    private final Date createTs;
}
