package dl.chatty.chat.view;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ChatView {
    private String id;
    private String title;
    private String createdBy;
    private Date createTs;
}
