package dl.chatty.chat.broker;

import java.security.Principal;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import dl.chatty.chat.protocol.ChatMessage;
import dl.chatty.datetime.DateTimeSupplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SimpBroker implements Broker<String, String, Principal> {

    public static final String MESSAGES_TOPIC = "/topic/messages";

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final DateTimeSupplier dateTimeSupplier;

    @Override
    public void onSend(String chatId, String message, Principal sender) {
        simpMessagingTemplate.convertAndSend(
                topicDestination(chatId),
                ChatMessage.of(
                        "id",
                        sender.getName(),
                        message,
                        dateTimeSupplier.get()));
    }

    String topicDestination(String destination) {
        return new StringBuilder(MESSAGES_TOPIC).append("/").append(destination).toString();
    }

    @Override
    public void onSubscribe(List<String> subscriptionId, String destination, Principal user) {
        log.info("User {} subscribed to {} with id {}", user.getName(), destination, subscriptionId);
    }

    @Override
    public void onUnsubscribe(List<String> subscriptionId, Principal user) {
        log.info("User {} unsubscribed from {}", user.getName(), subscriptionId);

    }

    @Override
    public void onDisconnect(Principal user) {
        log.info("User {} disconnected", user.getName());
    }

}
