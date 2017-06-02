package dl.chatty.chat.broker.event;

import static dl.chatty.MessageTestUtil.*;
import static org.mockito.Mockito.*;

import java.security.Principal;

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
        listener.onApplicationEvent(new SessionSubscribeEvent(
                "source",
                emptyByteMessage(destinationSessionHeaders("destination", "sid")),
                principal));

        verify(broker).onSubscribe("destination", principal, "sid");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailOnMissingSession() {
        listener.onApplicationEvent(new SessionSubscribeEvent(
                "source",
                emptyByteMessage(destinationHeaders("destination")),
                principal));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailOnMissingDestination() {

        listener.onApplicationEvent(new SessionSubscribeEvent(
                "source",
                emptyByteMessage(sessionHeaders("sid")),
                principal));
    }

}
