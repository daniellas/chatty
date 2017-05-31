package dl.chatty.chat.broker;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.PathMatcher;

import dl.chatty.chat.entity.Message;
import dl.chatty.chat.protocol.ChatMessage;
import dl.chatty.chat.repository.MessageRepository;
import io.reactivex.Observable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SimpBroker implements Broker<String, String, Principal> {

    public static final String MESSAGES_TOPIC = "/topic/messages";
    private static final String CHAT_ID_VARIABLE = "chatId";
    private static final String USER_VARIABLE = "user";
    public static final String DESTINATION_PATTERN = MESSAGES_TOPIC + "/{" + CHAT_ID_VARIABLE + "}/{" + USER_VARIABLE + "}";

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final MessageRepository messageRepository;

    private final PathMatcher destinationMatcher;

    private final ChatSubscriptionRegistry<String> subscriptionRegistry;

    @Override
    public void onSend(String chatId, String message, Principal sender) {
        Observable
                .fromCallable(() -> {
                    return messageRepository.create(chatId, Message.of(null, message, null, null), sender.getName());
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .subscribe(msg -> {
                    broadcast(chatId, msg);
                });
    }

    private void broadcast(String chatId, Message message) {
        subscriptionRegistry.chatDestinations(chatId).stream()
                .forEach(destination -> {
                    simpMessagingTemplate.convertAndSend(
                            topicDestination(destination),
                            ChatMessage.of(
                                    message.getId(),
                                    message.getFrom(),
                                    message.getMessage(),
                                    message.getSentTs()));
                });
    }

    private String topicDestination(String destination) {
        return MESSAGES_TOPIC + destination;
    }

    @Override
    public void onSubscribe(List<String> subscriptionIds, String destination, Principal user) {
        Observable.just(matchDestination(destination))
                .filter(Optional::isPresent)
                .map(variables -> {
                    return variables.get().get(CHAT_ID_VARIABLE);
                })
                .doOnNext(chatId -> {
                    log.info("User {} subscribed to chat {} ", user.getName(), chatId);
                })
                .subscribe(chatId -> {
                    String chatUserDestination = subscriptionRegistry.chatUserDestination(chatId, user.getName());

                    messageRepository.findForChat(chatId).stream().forEach(msg -> {
                        simpMessagingTemplate.convertAndSend(
                                topicDestination(chatUserDestination),
                                ChatMessage.of(msg.getId(), msg.getFrom(), msg.getMessage(), msg.getSentTs()));
                    });
                });

    }

    @Override
    public void onUnsubscribe(List<String> subscriptionIds, Principal user) {
        log.info("User {} unsubscribed from {}", user.getName(), subscriptionIds);

    }

    @Override
    public void onDisconnect(Principal user) {
        log.info("User {} disconnected", user.getName());
    }

    private Optional<Map<String, String>> matchDestination(String destination) {
        return Optional.ofNullable(destination)
                .filter(d -> destinationMatcher.match(DESTINATION_PATTERN, d))
                .map(d -> destinationMatcher.extractUriTemplateVariables(DESTINATION_PATTERN, d));
    }
}
