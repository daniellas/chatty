package dl.chatty.chat.broker;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.AntPathMatcher;

import dl.chatty.chat.entity.Chat;
import dl.chatty.chat.entity.Message;
import dl.chatty.chat.protocol.ChatMessage;
import dl.chatty.chat.repository.MessageRepository;
import dl.chatty.concurrency.ExecutorsProvider;
import dl.chatty.datetime.DateTimeSupplier;

@RunWith(MockitoJUnitRunner.class)
public class SimpBrokerTest {

    @Mock
    private SimpMessagingTemplate simpMessagetemplate;

    @Mock
    private DateTimeSupplier dateTimeSupplier;

    @Mock
    private Principal principal;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ExecutorsProvider executorsProvider;

    @Mock
    private ChatSubscriptionRegistry<String> subscriptionRegistry;

    @Mock
    private AntPathMatcher destinationMatcher;

    @Mock
    private Map<String, String> uriVariables;

    @Mock
    private MessageSendGuard messageSendGuard;

    @InjectMocks
    private SimpBroker broker;

    @Before
    public void before() {
        when(executorsProvider.messageExecutor()).thenReturn(Executors.newSingleThreadExecutor());
    }

    @Test
    public void shouldSendAsyncByDefault() {
        when(messageSendGuard.messageChat(any())).thenReturn(Optional.of(Chat.of("id", "title", "customer", new Date())));
        when(messageRepository.create(any(), any(), any())).thenReturn(Optional.of(Message.of("id", "message", "user", new Date())));

        broker.onSend("id", "message", principal);
        verify(executorsProvider).messageExecutor();
    }

    @Test
    public void shouldFollowOnSendFlow() {
        when(messageSendGuard.messageChat(any())).thenReturn(Optional.of(Chat.of("id", "title", "customer", new Date())));
        when(messageRepository.create(any(), any(), any())).thenReturn(Optional.of(Message.of("id", "message", "user", new Date())));
        when(subscriptionRegistry.chatDestinations(any())).thenReturn(Arrays.asList("destination1", "destination2"));

        broker.asyncObservable = false;
        broker.onSend("id", "message", principal);
        verify(messageSendGuard).messageChat("id");
        verify(messageRepository).create(any(), any(), any());
        verify(subscriptionRegistry).chatDestinations(any());
        verify(simpMessagetemplate, times(2)).convertAndSend(anyString(), any(ChatMessage.class));
    }

    @Test
    public void shouldSkipSendbyCustomerToNotOwnedChat() {
        when(messageSendGuard.messageChat(any())).thenReturn(Optional.empty());

        broker.asyncObservable = false;
        broker.onSend("id", "message", principal);
        verify(messageSendGuard).messageChat("id");
        verify(messageRepository, never()).create(any(), any(), any());
        verify(subscriptionRegistry, never()).chatDestinations(any());
        verify(simpMessagetemplate, never()).convertAndSend(anyString(), any(ChatMessage.class));
    }

    @Test
    public void shouldSkipSubscriptionOnInvalidDestination() {
        when(destinationMatcher.match(any(), any())).thenReturn(false);

        broker.onSubscribe(Arrays.asList("sub-1"), "/chat1", principal);
        verify(subscriptionRegistry, never()).create(any(), any(), any());
        verify(messageRepository, never()).findForChat(any());
    }

    @Test
    public void shouldFollowFlowOnSubscribe() {
        when(destinationMatcher.match(any(), any())).thenReturn(true);
        when(destinationMatcher.extractUriTemplateVariables(any(), any())).thenReturn(uriVariables);
        when(uriVariables.get(any())).thenReturn("chat1");
        when(principal.getName()).thenReturn("user1");
        when(messageRepository.findForChat(any())).thenReturn(Arrays.asList(Message.of(null, null, null, null)));

        List<String> subIds = Arrays.asList("sub-1");

        broker.onSubscribe(subIds, "/topic/messages/chat1/user1", principal);
        verify(subscriptionRegistry).create("chat1", "user1", subIds);
        verify(subscriptionRegistry).chatUserDestination("chat1", "user1");
        verify(messageRepository).findForChat("chat1");
        verify(simpMessagetemplate).convertAndSend(anyString(), any(ChatMessage.class));
    }

    @Test
    public void shouldFollowFlowOnUnsubscribe() {
        when(principal.getName()).thenReturn("user1");

        List<String> subIds = Arrays.asList("sub-1");

        broker.onUnsubscribe(subIds, principal);
        verify(subscriptionRegistry).remove("user1", subIds);
    }

    @Test
    public void shouldFollowFlowOnDisconnet() {
        when(principal.getName()).thenReturn("user1");

        broker.onDisconnect(principal);
        verify(subscriptionRegistry).remove("user1");
    }

}
