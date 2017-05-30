package dl.chatty.config;

import java.security.Principal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import dl.chatty.chat.broker.Broker;
import dl.chatty.chat.broker.SimpBroker;
import dl.chatty.chat.broker.event.ChatDisconnectEventListener;
import dl.chatty.chat.broker.event.ChatSubscribeEventListener;
import dl.chatty.chat.broker.event.ChatUnsubscribeEventListener;
import dl.chatty.datetime.DateTimeSupplier;
import dl.chatty.id.IdSupplier;

@Configuration
public class ChatBrokerConfig {


    @Bean
    public Broker<String, String, Principal> simpBroker(
            SimpMessagingTemplate simpMessagingTemplate,
            IdSupplier<String> messageIdSupplier,
            DateTimeSupplier dateTimeProvider) {
        return new SimpBroker(simpMessagingTemplate, dateTimeProvider);
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
}
