package dl.chatty.chat.repository;

import java.util.Collection;
import java.util.Optional;

import dl.chatty.chat.entity.Chat;

public interface ChatRepository {
    Collection<Chat> findAll();

    Optional<Chat> getOne(String id);
}
