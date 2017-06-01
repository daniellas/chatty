package dl.chatty.config;

import java.security.Principal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.AntPathMatcher;

import dl.chatty.chat.broker.Broker;
import dl.chatty.chat.broker.ChatSubscriptionRegistry;
import dl.chatty.chat.broker.DefaultChatSubscriptionRegistry;
import dl.chatty.chat.broker.MessageSendGuard;
import dl.chatty.chat.broker.RoleBasedMessageSendGuard;
import dl.chatty.chat.broker.SimpBroker;
import dl.chatty.chat.broker.event.ChatDisconnectEventListener;
import dl.chatty.chat.broker.event.ChatSubscribeEventListener;
import dl.chatty.chat.broker.event.ChatUnsubscribeEventListener;
import dl.chatty.chat.repository.ChatRepository;
import dl.chatty.chat.repository.MessageRepository;
import dl.chatty.concurrency.ExecutorsProvider;
import dl.chatty.id.IdSupplier;
import dl.chatty.security.AuthenticationSupplier;
import dl.chatty.security.UsernameSupplier;

@Configuration
public class ChatBrokerConfig {

    @Bean
    public ChatSubscriptionRegistry<String> defaultChatSubscriptionRegistry() {
        return new DefaultChatSubscriptionRegistry();
    }

    @Bean
    public Broker<String, String, Principal> simpBroker(
            SimpMessagingTemplate simpMessagingTemplate,
            IdSupplier<String> messageIdSupplier,
            MessageRepository messageRepo,
            ChatSubscriptionRegistry<String> subscriptionRegistry,
            ExecutorsProvider executorsProvider,
            MessageSendGuard messageSendGuard) {
        return new SimpBroker(simpMessagingTemplate, messageRepo, new AntPathMatcher("/"), subscriptionRegistry, executorsProvider, messageSendGuard);
    }

    @Bean
    public ChatSubscribeEventListener subscribeEventListener(Broker<String, ?, Principal> broker) {
        return new ChatSubscribeEventListener(broker);
    }

    @Bean
    public ChatUnsubscribeEventListener unsubscribeEventListener(Broker<String, ?, Principal> broker) {
        return new ChatUnsubscribeEventListener(broker);
    }

    @Bean
    public ChatDisconnectEventListener disconnectEventListener(Broker<String, ?, Principal> broker) {
        return new ChatDisconnectEventListener(broker);
    }

    @Bean
    public MessageSendGuard messageSendGuard(ChatRepository chatRepository, AuthenticationSupplier authenticationSupplier, UsernameSupplier usernameSupplier) {
        return new RoleBasedMessageSendGuard(chatRepository, authenticationSupplier, usernameSupplier);
    }
}
