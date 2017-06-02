package dl.chatty;

import java.util.HashMap;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;

public class MessageTestUtil {

    public static Message<byte[]> emptyByteMessage() {
        return MessageBuilder.createMessage(new byte[0], new MessageHeaders(new HashMap<>()));
    }

    public static Message<byte[]> emptyByteMessage(MessageHeaders headers) {
        return MessageBuilder.createMessage(new byte[0], headers);
    }

    public static MessageHeaders sessionHeaders(String sessionId) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create();

        accessor.setSessionId(sessionId);

        return accessor.getMessageHeaders();
    }

    public static MessageHeaders destinationHeaders(String destination) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create();

        accessor.setDestination(destination);
        
        return accessor.getMessageHeaders();
    }

    public static MessageHeaders destinationSessionHeaders(String destination, String sessionId) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create();

        accessor.setDestination(destination);
        accessor.setSessionId(sessionId);

        return accessor.getMessageHeaders();
    }

}
