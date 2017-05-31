package dl.chatty.chat.broker.event;

import static dl.chatty.MessageTestUtil.*;
import static org.mockito.Mockito.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import dl.chatty.chat.broker.Broker;

@RunWith(MockitoJUnitRunner.class)
public class ChatSubscribeEventListenerTest {

    @Mock
    private Broker<String, String, Principal> broker;

    @Mock
    private Principal principal;

    @InjectMocks
    private ChatSubscribeEventListener listener;

    @Test
    public void shouldCallBrokerOnEvent() {
        List<String> nativeHeaders = new ArrayList<>();

        listener.onApplicationEvent(new SessionSubscribeEvent(
                "source",
                emptyByteMessage(subscriptionDestinationHeaders("destination", nativeHeaders)),
                principal));

        verify(broker).onSubscribe(nativeHeaders, "destination", principal);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailOnMissingSubscriptions() {
        listener.onApplicationEvent(new SessionSubscribeEvent(
                "source",
                emptyByteMessage(destinationHeaders("destination")),
                principal));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailOnMissingDestination() {
        List<String> nativeHeaders = new ArrayList<>();

        listener.onApplicationEvent(new SessionSubscribeEvent(
                "source",
                emptyByteMessage(subscriptionHeaders(nativeHeaders)),
                principal));
    }

}
