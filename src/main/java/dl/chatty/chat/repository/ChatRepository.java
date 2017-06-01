package dl.chatty.chat.repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dl.chatty.chat.entity.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> create(Chat chat, String creator);

    Collection<Chat> findAll(String creator);

    Optional<Chat> getOne(String id);
}
