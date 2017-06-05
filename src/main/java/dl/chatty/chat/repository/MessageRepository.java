package dl.chatty.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import dl.chatty.chat.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Transactional(readOnly = true)
    @Query("select m from Message m where chat.id=:chatId order by id")
    List<Message> findByChatIdOrderById(@Param("chatId") Long chatId);
}
