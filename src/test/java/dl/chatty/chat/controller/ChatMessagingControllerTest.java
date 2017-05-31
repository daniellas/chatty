package dl.chatty.chat.controller;

import java.security.Principal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import dl.chatty.chat.broker.Broker;

@RunWith(MockitoJUnitRunner.class)
public class ChatMessagingControllerTest {

    @Mock
    private Broker<String, String, Principal> broker;

    @InjectMocks
    private ChatMessagingController controller;

    @Test
    public void shouldSendMessage() {
        controller.handleMessage("msg", "id", null);

        Mockito.verify(broker).onSend("id", "msg", null);
    }
}
