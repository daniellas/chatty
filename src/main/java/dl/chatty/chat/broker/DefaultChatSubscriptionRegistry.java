package dl.chatty.chat.broker;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import dl.chatty.chat.entity.ChatSubscription;
import dl.chatty.chat.repository.ChatSubscriptionRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RequiredArgsConstructor
public class DefaultChatSubscriptionRegistry implements ChatSubscriptionRegistry<Long> {

    private final ChatSubscriptionRepository chatSubscriptionRepository;

    @Override
    public void create(Long chatId, String user, String sessionId) {
        if (chatSubscriptionRepository.findByChatAndUserAndSession(chatId, user, sessionId) == null) {
            chatSubscriptionRepository.save(ChatSubscription.of(null, user, chatId, sessionId));
        } else {
            log.warn("Subscription {}/{}/{} already exists, skipping creation", chatId, user, sessionId);
        }
    }

    @Override
    public void remove(String user, String sessionId) {
        chatSubscriptionRepository.deleteInBatch(chatSubscriptionRepository.findByUserAndSession(user, sessionId));
    }

    @Override
    public void remove(String user) {
        chatSubscriptionRepository.deleteInBatch(chatSubscriptionRepository.findByUser(user));
    }

    @Override
    public Collection<Subscription> chatSubscriptions(Long chatId) {
        return chatSubscriptionRepository.findByChat(chatId).stream()
                .map(s -> RegistryChatSubscription.of(s.getUser(), s.getSession()))
                .collect(Collectors.toList());
    }

    @Data
    @RequiredArgsConstructor(staticName = "of")
    static class RegistryChatSubscription implements Subscription {
        private final String user;
        private final String sessionId;
    }
}
