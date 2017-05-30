package dl.chatty.chat.broker;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.messaging.MessageHeaders;

public class StompHeadersUtil {

    private final static String DESTINATION_HEADER_KEY = "simpDestination";

    private final static String NATIVE_HEADERS_KEY = "nativeHeaders";

    private final static String NATIVE_HEADERS_ID_KEY = "id";

    public static String destination(MessageHeaders headers) {
        return Optional.ofNullable(headers.get(DESTINATION_HEADER_KEY))
                .map(Object::toString)
                .orElseThrow(() -> {
                    return new IllegalArgumentException("Missing destination header");
                });
    }

    public static List<String> subscriptionIds(MessageHeaders headers) {
        return Optional.ofNullable(headers.get(NATIVE_HEADERS_KEY))
                .map(StompHeadersUtil::toMap)
                .map(h -> h.get(NATIVE_HEADERS_ID_KEY))
                .map(StompHeadersUtil::toList)
                .map(List::stream)
                .map(l -> l.map(Object::toString).collect(Collectors.toList()))
                .orElseThrow(() -> {
                    return new IllegalArgumentException("Missing subscription id header");
                });
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> toMap(Object value) {
        return (Map<String, Object>) value;
    }

    @SuppressWarnings("unchecked")
    private static List<Object> toList(Object value) {
        return (List<Object>) value;
    }

}
