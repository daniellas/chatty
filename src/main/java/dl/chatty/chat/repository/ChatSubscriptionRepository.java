package dl.chatty.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dl.chatty.chat.entity.ChatSubscription;

public interface ChatSubscriptionRepository extends JpaRepository<ChatSubscription, Long> {

    ChatSubscription findByChatAndUserAndSession(Long chat, String user, String session);

    List<ChatSubscription> findByUserAndSession(String user, String session);

    List<ChatSubscription> findByUser(String user);

    List<ChatSubscription> findByChat(Long chat);

}
