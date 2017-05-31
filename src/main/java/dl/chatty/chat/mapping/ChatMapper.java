package dl.chatty.chat.mapping;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import dl.chatty.chat.entity.Chat;
import dl.chatty.chat.view.ChatView;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatMapper {

    public static ChatView toView(Chat entity) {
        return new ChatView(entity.getId(), entity.getTitle(), entity.getCreatedBy(), entity.getCreateTs());
    }

    public static List<ChatView> toViewList(Collection<Chat> entities) {
        return entities.stream().map(ChatMapper::toView).collect(Collectors.toList());
    }

    public static Chat toEntity(ChatView view) {
        return Chat.of(view.getId(), view.getTitle(), view.getCreatedBy(), view.getCreateTs());
    }
}
