package dl.chatty.chat.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import dl.chatty.chat.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long>{

    Collection<Message> findByChatIdOrderById(Long chatId);
}
