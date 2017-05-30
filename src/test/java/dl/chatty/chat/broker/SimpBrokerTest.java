package dl.chatty.chat.broker;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import dl.chatty.chat.protocol.ChatMessage;
import dl.chatty.datetime.DateTimeSupplier;

@Ignore
@RunWith(MockitoJUnitRunner.class)
public class SimpBrokerTest {

    @Mock
    private SimpMessagingTemplate simpMessagetemplate;

    @Mock
    private DateTimeSupplier dateTimeSupplier;

    @InjectMocks
    private SimpBroker messenger;

    @Captor
    private ArgumentCaptor<ChatMessage> message;

    @Test
    public void shouldSendToCorrectDestinationOnDispatch() {
        ArgumentCaptor<String> destination = ArgumentCaptor.forClass(String.class);

        messenger.onSend("id", "message", null);

        verify(simpMessagetemplate).convertAndSend(destination.capture(), anyString());
        assertEquals(SimpBroker.MESSAGES_TOPIC + "/id", destination.getValue());
    }

    @Test
    public void shouldSetMessageContent() {
        messenger.onSend("id", "message", null);

        verify(simpMessagetemplate).convertAndSend(anyString(), message.capture());
        assertEquals("message", message.getValue().getMessage());
    }

    @Test
    public void shouldGetMessageSendTimestamptFromSupplier() {
        messenger.onSend("id", "message", null);

        verify(simpMessagetemplate).convertAndSend(anyString(), message.capture());
        verify(dateTimeSupplier).get();
    }
}
