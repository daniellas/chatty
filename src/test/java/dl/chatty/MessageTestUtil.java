package dl.chatty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import dl.chatty.chat.broker.StompHeadersUtil;

public class MessageTestUtil {

    public static Message<byte[]> emptyByteMessage() {
        return MessageBuilder.createMessage(new byte[0], new MessageHeaders(new HashMap<>()));
    }

    public static Message<byte[]> emptyByteMessage(MessageHeaders headers) {
        return MessageBuilder.createMessage(new byte[0], headers);
    }

    public static MessageHeaders subscriptionDestinationHeaders(String destination, List<String> nativeHeadersContent) {
        Map<String, Object> headers = new HashMap<>();
        Map<String, Object> nativeHeaders = new HashMap<>();

        nativeHeaders.put(StompHeadersUtil.NATIVE_HEADERS_ID_KEY, nativeHeadersContent);
        headers.put(StompHeadersUtil.NATIVE_HEADERS_KEY, nativeHeaders);
        headers.put(StompHeadersUtil.DESTINATION_HEADER_KEY, destination);

        return new MessageHeaders(headers);
    }

    public static MessageHeaders destinationHeaders(String destination) {
        Map<String, Object> headers = new HashMap<>();

        headers.put(StompHeadersUtil.DESTINATION_HEADER_KEY, destination);

        return new MessageHeaders(headers);
    }

    public static MessageHeaders subscriptionHeaders(List<String> nativeHeadersContent) {
        Map<String, Object> headers = new HashMap<>();
        Map<String, Object> nativeHeaders = new HashMap<>();

        nativeHeaders.put(StompHeadersUtil.NATIVE_HEADERS_ID_KEY, nativeHeadersContent);
        headers.put(StompHeadersUtil.NATIVE_HEADERS_KEY, nativeHeaders);

        return new MessageHeaders(headers);
    }

}
