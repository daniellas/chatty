package dl.chatty.chat.repository;

import java.util.Collection;
import java.util.Optional;

import dl.chatty.chat.entity.Chat;

public interface ChatRepository {

    Optional<Chat> create(Chat chat, String creator);

    Collection<Chat> findAll(String creator);

    Optional<Chat> getOne(String id);
}
