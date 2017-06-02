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

import dl.chatty.chat.broker.ChatSubscriptionRegistry.Subscription;
import dl.chatty.chat.broker.DefaultChatSubscriptionRegistry.RegistryChatSubscription;
import dl.chatty.chat.entity.Chat;
import dl.chatty.chat.entity.Message;
import dl.chatty.chat.protocol.ChatMessage;
import dl.chatty.chat.repository.MessageRepository;
import dl.chatty.concurrency.ExecutorsProvider;

@RunWith(MockitoJUnitRunner.class)
public class SimpBrokerTest {

    @Mock
    private SimpMessagingTemplate simpMessagetemplate;

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
    private MessageSendGuard<Long, Principal> messageSendGuard;

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

        broker.onSend(1l, "message", principal, null);
        verify(executorsProvider).messageExecutor();
    }

    @Test
    public void shouldFollowOnSendFlow() {
        List<Subscription> subs = Arrays.asList(
                RegistryChatSubscription.of("user1", null),
                RegistryChatSubscription.of("user2", null));

        when(messageSendGuard.messageChat(any(), any())).thenReturn(Optional.of(Chat.of(1l, "title", "customer", new Date())));
        when(messageRepository.save(any(Message.class))).thenReturn(Message.of(1l, "message", "user", new Date(), null));
        when(subscriptionRegistry.chatSubscriptions(any())).thenReturn(subs);

        broker.asyncObservable = false;
        broker.onSend(1l, "message", principal, null);
        verify(messageSendGuard).messageChat(any(), any());
        verify(messageRepository).save(any(Message.class));
        verify(subscriptionRegistry).chatSubscriptions(any());
        verify(simpMessagetemplate, times(2)).convertAndSendToUser(anyString(), anyString(), any(ChatMessage.class));
    }

    @Test
    public void shouldSkipSendbyCustomerToNotOwnedChat() {
        when(messageSendGuard.messageChat(any(), any())).thenReturn(Optional.empty());

        broker.asyncObservable = false;
        broker.onSend(1l, "message", principal, null);
        verify(messageSendGuard).messageChat(any(), any());
        verify(messageRepository, never()).save(any(Message.class));
        verify(subscriptionRegistry, never()).chatSubscriptions(any());
        verify(simpMessagetemplate, never()).convertAndSend(anyString(), any(ChatMessage.class));
    }

    @Test
    public void shouldSkipSubscriptionOnInvalidDestination() {
        when(destinationMatcher.match(any(), any())).thenReturn(false);

        broker.onSubscribe("/chat1", principal, "sid");
        verify(subscriptionRegistry, never()).create(any(), any(), any());
        verify(messageRepository, never()).findByChatIdOrderById(any());
    }

    @Test
    public void shouldFollowFlowOnSubscribe() {
        when(destinationMatcher.match(any(), any())).thenReturn(true);
        when(destinationMatcher.extractUriTemplateVariables(any(), any())).thenReturn(uriVariables);
        when(uriVariables.get(any())).thenReturn("1");
        when(principal.getName()).thenReturn("user1");
        when(messageRepository.findByChatIdOrderById(any())).thenReturn(Arrays.asList(Message.of(null, null, null, null, null)));

        broker.onSubscribe("/topic/messages/1/user1", principal, "sid");
        verify(subscriptionRegistry).create(1l, "user1", "sid");
        verify(messageRepository).findByChatIdOrderById(1l);
        verify(simpMessagetemplate).convertAndSendToUser(anyString(), anyString(), any(ChatMessage.class));
    }

    @Test
    public void shouldFollowFlowOnUnsubscribe() {
        when(principal.getName()).thenReturn("user1");

        broker.onUnsubscribe(principal, "sid");
        verify(subscriptionRegistry).remove("user1", "sid");
    }

    @Test
    public void shouldFollowFlowOnDisconnet() {
        when(principal.getName()).thenReturn("user1");

        broker.onDisconnect(principal, null);
        verify(subscriptionRegistry).remove("user1");
    }

}
