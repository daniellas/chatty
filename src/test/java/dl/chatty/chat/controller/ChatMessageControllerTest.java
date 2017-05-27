package dl.chatty.chat.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import dl.chatty.chat.broker.Broker;

@RunWith(MockitoJUnitRunner.class)
public class ChatMessageControllerTest {

    @Mock
    private Broker<String, String> broker;

    @InjectMocks
    private ChatMessageController controller;

    @Test
    public void shouldDispatchMessage() {
        controller.handleMessage("msg", "id", null);

        Mockito.verify(broker).dispatch("id", "msg", null);
    }
}
