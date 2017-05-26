package dl.chatty.chat;

import java.util.Collection;
import java.util.Optional;

public interface ChatRepository {
    Collection<Chat> findAll();

    Optional<Chat> getOne(String id);
}
