package dl.chatty.chat.broker;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public static final String MESSAGES_TOPIC = "/topic/messages";
    private static final String CHAT_ID_VARIABLE = "chatId";
    private static final String USER_VARIABLE = "user";
    public static final String DESTINATION_PATTERN = MESSAGES_TOPIC + "/{" + CHAT_ID_VARIABLE + "}/{" + USER_VARIABLE + "}";

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final MessageRepository messageRepository;

    private final PathMatcher destinationMatcher;

    private final ChatSubscriptionRegistry<Long> subscriptionRegistry;

    private final ExecutorsProvider executorsProvider;

    private final MessageSendGuard messageSendGuard;

    boolean asyncObservable = true;

    @Override
    public void onSend(Long chatId, String message, Principal sender) {
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
    public void onSubscribe(List<String> subscriptionIds, String destination, Principal user) {
        Observable.just(matchDestination(destination))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(variables -> {
                    return Long.parseLong(variables.get(CHAT_ID_VARIABLE));
                })
                .doOnNext(chatId -> {
                    log.info("User {} subscribed to chat {} with subscription ids {}", user.getName(), chatId, subscriptionIds);
                })
                .subscribe(chatId -> {
                    subscriptionRegistry.create(chatId, user.getName(), subscriptionIds);

                    String chatUserDestination = subscriptionRegistry.chatUserDestination(chatId, user.getName());

                    messageRepository.findByChatId(chatId).stream().forEach(msg -> {
                        simpMessagingTemplate.convertAndSend(
                                topicDestination(chatUserDestination),
                                ChatMessage.of(msg.getId(), msg.getSender(), msg.getMessage(), msg.getSentTs()));
                    });
                });

    }

    @Override
    public void onUnsubscribe(List<String> subscriptionIds, Principal user) {
        Observable.just(user)
                .doOnNext(u -> {
                    log.info("User {} unsubscribed from {}", u.getName(), subscriptionIds);
                })
                .subscribe(u -> {
                    subscriptionRegistry.remove(u.getName(), subscriptionIds);
                });
    }

    @Override
    public void onDisconnect(Principal user) {
        Observable.just(user)
                .doOnNext(u -> {
                    log.info("User {} disconnected", u.getName());
                }).subscribe(u -> {
                    subscriptionRegistry.remove(u.getName());
                });
    }

    private void broadcast(Long chatId, Message message) {
        subscriptionRegistry.chatDestinations(chatId).stream()
                .forEach(destination -> {
                    simpMessagingTemplate.convertAndSend(
                            topicDestination(destination),
                            ChatMessage.of(
                                    message.getId(),
                                    message.getSender(),
                                    message.getMessage(),
                                    message.getSentTs()));
                });
    }

    private String topicDestination(String destination) {
        return MESSAGES_TOPIC + destination;
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

}
