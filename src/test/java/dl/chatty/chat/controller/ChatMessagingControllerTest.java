package dl.chatty.chat.controller;

import java.security.Principal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import dl.chatty.MessageTestUtil;
import dl.chatty.chat.broker.Broker;

@RunWith(MockitoJUnitRunner.class)
public class ChatMessagingControllerTest {

    @Mock
    private Broker<Long, String, Principal> broker;

    @InjectMocks
    private ChatMessagingController controller;

    @Test
    public void shouldSendMessage() {
        controller.handleMessage("msg", 1l, null, MessageTestUtil.emptyByteMessage(MessageTestUtil.sessionHeaders("sid")));

        Mockito.verify(broker).onSend(1l, "msg", null, "sid");
    }
}
