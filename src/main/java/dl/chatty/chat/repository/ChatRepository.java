package dl.chatty.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dl.chatty.chat.entity.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("select c from Chat c where c.createdBy=:createdBy or :createdBy is null order by id desc")
    List<Chat> findByCreatedBy(@Param("createdBy") String createdBy);

}
