package dl.chatty.chat.broker;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.security.Principal;
import java.util.ArrayList;

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

// TODO Enable
@Ignore
@RunWith(MockitoJUnitRunner.class)
public class SimpBrokerTest {

    @Mock
    private SimpMessagingTemplate simpMessagetemplate;

    @Mock
    private DateTimeSupplier dateTimeSupplier;

    @Mock
    private Principal principal;

    @InjectMocks
    private SimpBroker broker;

    @Captor
    private ArgumentCaptor<ChatMessage> message;

    @Test
    public void shouldSendToCorrectDestinationOnDispatch() {
        ArgumentCaptor<String> destination = ArgumentCaptor.forClass(String.class);

        broker.onSend("id", "message", principal);

        verify(simpMessagetemplate).convertAndSend(destination.capture(), anyString());
        assertEquals(SimpBroker.MESSAGES_TOPIC + "/id", destination.getValue());
    }

    @Test
    public void shouldSetMessageContent() {
        broker.onSend("id", "message", principal);

        verify(simpMessagetemplate).convertAndSend(anyString(), message.capture());
        assertEquals("message", message.getValue().getMessage());
    }

    @Test
    public void shouldGetMessageSendTimestamptFromSupplier() {
        broker.onSend("id", "message", principal);

        verify(simpMessagetemplate).convertAndSend(anyString(), message.capture());
        verify(dateTimeSupplier).get();
    }

    @Test
    public void shouldRegisterSubscriptionOnSubscribe() {
        when(principal.getName()).thenReturn("customer");

        broker.onSubscribe(new ArrayList<>(), "/topic/1/customer", principal);
    }

    @Test
    public void shouldUnregisterSubscriptionOnUnsubscribe() {
        when(principal.getName()).thenReturn("customer");

        broker.onUnsubscribe(new ArrayList<>(), principal);
    }

    @Test
    public void shouldUnregisterAllSubscriptionsOnDisconnect() {
        when(principal.getName()).thenReturn("customer");

        broker.onDisconnect(principal);
    }

}
