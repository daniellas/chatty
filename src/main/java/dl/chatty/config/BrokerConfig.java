package dl.chatty.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import dl.chatty.chat.broker.Broker;
import dl.chatty.chat.broker.SimpBroker;
import dl.chatty.datetime.DateTimeSupplier;

@Configuration
public class BrokerConfig {

    @Bean
    public Broker<String, String> simpBroker(SimpMessagingTemplate simpMessagingTemplate, DateTimeSupplier dateTimeProvider) {
        return new SimpBroker(simpMessagingTemplate, dateTimeProvider);
    }
}
