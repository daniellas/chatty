package dl.chatty.chat.broker;

import java.util.Optional;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StompHeadersUtil {

    public static String destination(Message<?> message) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(message);

        return Optional.ofNullable(accessor.getDestination())
                .orElseThrow(() -> {
                    return new IllegalArgumentException("Missing destination header");
                });
    }

    public static String sessionId(Message<?> message) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(message);
        return Optional.ofNullable(accessor.getSessionId())
                .orElseThrow(() -> {
                    return new IllegalArgumentException("Missing session id header");
                });
    }

}
