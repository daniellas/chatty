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
    private ChatSubscriptionRegistry<Long> subscriptionRegistry;

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
        when(messageSendGuard.messageChat(any(), any())).thenReturn(Optional.of(Chat.of(1l, "title", "customer", new Date())));
        when(messageRepository.save(any(Message.class))).thenReturn(Message.of(1l, "message", "user", new Date(), null));

        broker.onSend(1l, "message", principal);
        verify(executorsProvider).messageExecutor();
    }

    @Test
    public void shouldFollowOnSendFlow() {
        when(messageSendGuard.messageChat(any(), any())).thenReturn(Optional.of(Chat.of(1l, "title", "customer", new Date())));
        when(messageRepository.save(any(Message.class))).thenReturn(Message.of(1l, "message", "user", new Date(), null));
        when(subscriptionRegistry.chatDestinations(any())).thenReturn(Arrays.asList("destination1", "destination2"));

        broker.asyncObservable = false;
        broker.onSend(1l, "message", principal);
        verify(messageSendGuard).messageChat(any(), any());
        verify(messageRepository).save(any(Message.class));
        verify(subscriptionRegistry).chatDestinations(any());
        verify(simpMessagetemplate, times(2)).convertAndSend(anyString(), any(ChatMessage.class));
    }

    @Test
    public void shouldSkipSendbyCustomerToNotOwnedChat() {
        when(messageSendGuard.messageChat(any(), any())).thenReturn(Optional.empty());

        broker.asyncObservable = false;
        broker.onSend(1l, "message", principal);
        verify(messageSendGuard).messageChat(any(), any());
        verify(messageRepository, never()).save(any(Message.class));
        verify(subscriptionRegistry, never()).chatDestinations(any());
        verify(simpMessagetemplate, never()).convertAndSend(anyString(), any(ChatMessage.class));
    }

    @Test
    public void shouldSkipSubscriptionOnInvalidDestination() {
        when(destinationMatcher.match(any(), any())).thenReturn(false);

        broker.onSubscribe(Arrays.asList("sub-1"), "/chat1", principal);
        verify(subscriptionRegistry, never()).create(any(), any(), any());
        verify(messageRepository, never()).findByChatId(any());
    }

    @Test
    public void shouldFollowFlowOnSubscribe() {
        when(destinationMatcher.match(any(), any())).thenReturn(true);
        when(destinationMatcher.extractUriTemplateVariables(any(), any())).thenReturn(uriVariables);
        when(uriVariables.get(any())).thenReturn("1");
        when(principal.getName()).thenReturn("user1");
        when(messageRepository.findByChatId(any())).thenReturn(Arrays.asList(Message.of(null, null, null, null, null)));

        List<String> subIds = Arrays.asList("sub-1");

        broker.onSubscribe(subIds, "/topic/messages/1/user1", principal);
        verify(subscriptionRegistry).create(1l, "user1", subIds);
        verify(subscriptionRegistry).chatUserDestination(1l, "user1");
        verify(messageRepository).findByChatId(1l);
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
