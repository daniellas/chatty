package dl.chatty.chat.broker;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.PathMatcher;

import dl.chatty.chat.entity.Chat;
import dl.chatty.chat.entity.Message;
import dl.chatty.chat.protocol.ChatMessage;
import dl.chatty.chat.repository.MessageRepository;
import dl.chatty.concurrency.ExecutorsProvider;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SimpBroker implements Broker<Long, String, Principal> {

    public static final String USER_DESTINATION_PREFIX = "/queue/messages/";

    public static final String DESTINATION_PREFIX = "/user/queue/messages/";

    private static final String CHAT_ID_VARIABLE = "chatId";

    public static final String DESTINATION_PATTERN = DESTINATION_PREFIX + "{" + CHAT_ID_VARIABLE + "}";

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final MessageRepository messageRepository;

    private final PathMatcher destinationMatcher;

    private final ChatSubscriptionRegistry<Long> subscriptionRegistry;

    private final ExecutorsProvider executorsProvider;

    private final MessageSendGuard<Long, Principal> messageSendGuard;

    boolean asyncObservable = true;

    @Override
    public void onSend(Long chatId, String message, Principal sender, String sessionId) {
        Optional<Chat> guardedChat = messageSendGuard.messageChat(chatId, sender);

        Observable<Optional<Chat>> observable = Observable.just(guardedChat);

        subscribeOnShedulerIfAsync(observable)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(c -> {
                    return messageRepository.save(Message.of(null, message, sender.getName(), null, c));
                })
                .subscribe(msg -> {
                    broadcast(chatId, msg);
                });
    }

    @Override
    public void onSubscribe(String destination, Principal user, String sessionId) {
        Observable.just(matchDestination(destination))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(variables -> {
                    return Long.parseLong(variables.get(CHAT_ID_VARIABLE));
                })
                .doOnNext(chatId -> {
                    log.info("User {} subscribed to chat {} with session id {}", user.getName(), chatId, sessionId);
                }).subscribe(chatId -> {
                    subscriptionRegistry.create(chatId, user.getName(), sessionId);
                    sendChatHistoryToUserSession(chatId, user.getName(), sessionId);
                });

    }

    @Override
    public void onUnsubscribe(Principal user, String sessionId) {
        Observable.just(user)
                .doOnNext(u -> {
                    log.info("User {} unsubscribed from session {}", u.getName(), sessionId);
                })
                .subscribe(u -> {
                    subscriptionRegistry.remove(u.getName(), sessionId);
                });
    }

    @Override
    public void onDisconnect(Principal user, String sessionId) {
        Observable.just(user)
                .doOnNext(u -> {
                    log.info("User {} disconnected", u.getName());
                }).subscribe(u -> {
                    subscriptionRegistry.remove(u.getName());
                });
    }

    private void sendChatHistoryToUserSession(Long chatId, String user, String sessionId) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);

        accessor.setSessionId(sessionId);
        accessor.setLeaveMutable(true);

        List<ChatMessage> messages = messageRepository.findByChatIdOrderById(chatId).stream()
                .map(m -> ChatMessage.of(m.getId(), m.getSender(), m.getMessage(), m.getSentTs()))
                .collect(Collectors.toList());

        log.info("Sending chat history length {} to subscribed user {} in session{}", messages.size(), user, sessionId);
        simpMessagingTemplate.convertAndSendToUser(user, userChatDestination(chatId), messages, accessor.getMessageHeaders());
    }

    private void broadcast(Long chatId, Message message) {
        subscriptionRegistry.chatSubscriptions(chatId).stream()
                .forEach(sub -> {
                    simpMessagingTemplate.convertAndSendToUser(
                            sub.getUser(),
                            userChatDestination(chatId),
                            ChatMessage.of(
                                    message.getId(),
                                    message.getSender(),
                                    message.getMessage(),
                                    message.getSentTs()));
                });
    }

    private Optional<Map<String, String>> matchDestination(String destination) {
        return Optional.ofNullable(destination)
                .filter(d -> destinationMatcher.match(DESTINATION_PATTERN, d))
                .map(d -> destinationMatcher.extractUriTemplateVariables(DESTINATION_PATTERN, d));
    }

    private <T> Observable<T> subscribeOnShedulerIfAsync(Observable<T> observable) {
        if (asyncObservable) {
            return observable.subscribeOn(Schedulers.from(executorsProvider.messageExecutor()));
        }

        return observable;
    }

    private String userChatDestination(Long chatId) {
        return USER_DESTINATION_PREFIX + chatId;
    }
}
