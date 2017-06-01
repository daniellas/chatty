package dl.chatty.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dl.chatty.chat.entity.ChatSubscription;

public interface ChatSubscriptionRepository extends JpaRepository<ChatSubscription, Long> {

    List<ChatSubscription> findByUserAndSub(String user, String sub);

    List<ChatSubscription> findByUser(String user);

    List<ChatSubscription> findByChat(String chat);

}
