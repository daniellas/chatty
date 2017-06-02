package dl.chatty.chat.view;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatView {
    private Long id;
    @NotNull
    @Size(min = 1)
    private String title;
    private String createdBy;
    private Date createTs;
}
