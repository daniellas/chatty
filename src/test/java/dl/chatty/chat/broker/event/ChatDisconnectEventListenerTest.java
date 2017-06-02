package dl.chatty.chat.broker.event;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.security.Principal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import dl.chatty.MessageTestUtil;
import dl.chatty.chat.broker.Broker;

@RunWith(MockitoJUnitRunner.class)
public class ChatDisconnectEventListenerTest {

    @Mock
    private Broker<String, String, Principal> broker;

    @InjectMocks
    private ChatDisconnectEventListener listener;

    @Test
    public void shouldCallBrokerOnEvent() {
        listener.onApplicationEvent(new SessionDisconnectEvent(
                "source",
                MessageTestUtil.emptyByteMessage(MessageTestUtil.sessionHeaders("sid")),
                "id",
                CloseStatus.NORMAL));
        verify(broker).onDisconnect(any(), any());
    }

}
