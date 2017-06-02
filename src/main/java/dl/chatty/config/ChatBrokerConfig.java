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
import dl.chatty.chat.repository.ChatSubscriptionRepository;
import dl.chatty.chat.repository.MessageRepository;
import dl.chatty.concurrency.ExecutorsProvider;

@Configuration
public class ChatBrokerConfig {

    @Bean
    public ChatSubscriptionRegistry<Long> defaultChatSubscriptionRegistry(ChatSubscriptionRepository chatSubscriptionRepository) {
        return new DefaultChatSubscriptionRegistry(chatSubscriptionRepository);
    }

    @Bean
    public Broker<Long, String, Principal> simpBroker(
            SimpMessagingTemplate simpMessagingTemplate,
            MessageRepository messageRepo,
            ChatSubscriptionRegistry<Long> subscriptionRegistry,
            ExecutorsProvider executorsProvider,
            MessageSendGuard<Long, Principal> messageSendGuard) {
        return new SimpBroker(simpMessagingTemplate, messageRepo, new AntPathMatcher("/"), subscriptionRegistry, executorsProvider, messageSendGuard);
    }

    @Bean
    public ChatSubscribeEventListener subscribeEventListener(Broker<Long, ?, Principal> broker) {
        return new ChatSubscribeEventListener(broker);
    }

    @Bean
    public ChatUnsubscribeEventListener unsubscribeEventListener(Broker<Long, ?, Principal> broker) {
        return new ChatUnsubscribeEventListener(broker);
    }

    @Bean
    public ChatDisconnectEventListener disconnectEventListener(Broker<Long, ?, Principal> broker) {
        return new ChatDisconnectEventListener(broker);
    }

    @Bean
    public MessageSendGuard<Long, Principal> messageSendGuard(ChatRepository chatRepository) {
        return new RoleBasedMessageSendGuard(chatRepository);
    }
}
