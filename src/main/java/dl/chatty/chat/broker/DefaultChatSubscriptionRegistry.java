package dl.chatty.chat.broker;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import dl.chatty.chat.entity.ChatSubscription;
import dl.chatty.chat.repository.ChatSubscriptionRepository;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
public class DefaultChatSubscriptionRegistry implements ChatSubscriptionRegistry<Long> {

    private static final String SEPARATOR = "/";

    private final ChatSubscriptionRepository chatSubscriptionRepository;

    @Override
    public void create(Long chatId, String user, Collection<String> subscriptionIds) {
        chatSubscriptionRepository.save(ChatSubscription.of(null, user, chatId, subscriptionIds.toString()));
    }

    @Override
    public void remove(String user, Collection<String> subscriptionIds) {
        chatSubscriptionRepository.deleteInBatch(chatSubscriptionRepository.findByUserAndSub(user, subscriptionIds.toString()));
    }

    @Override
    public void remove(String user) {
        chatSubscriptionRepository.deleteInBatch(chatSubscriptionRepository.findByUser(user));
    }

    @Override
    public Collection<String> chatDestinations(Long chatId) {
        return chatSubscriptionRepository.findByChat(chatId).stream()
                .map(s -> SEPARATOR + s.getChat() + SEPARATOR + s.getUser()).collect(Collectors.toList());
    }

    @Override
    public String chatUserDestination(Long chatId, String user) {
        return SEPARATOR + chatId + SEPARATOR + user;
    }

}
