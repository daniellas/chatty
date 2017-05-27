package dl.chatty.chat.broker;

import java.security.Principal;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import dl.chatty.chat.protocol.ChatMessage;
import dl.chatty.datetime.DateTimeSupplier;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimpBroker implements Broker<String, String> {

    public static final String MESSAGES_TOPIC = "/topic/messages";

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final DateTimeSupplier dateTimeSupplier;

    @Override
    public void dispatch(String destination, String message, Principal sender) {
        simpMessagingTemplate.convertAndSend("/topic/messages/" + destination, new ChatMessage(null, sender.getName(), message, dateTimeSupplier.get()));
    }

}
