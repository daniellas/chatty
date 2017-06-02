package dl.chatty.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dl.chatty.chat.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select m from Message m where chat.id=:chatId order by id")
    List<Message> findByChatIdOrderById(@Param("chatId") Long chatId);
}
