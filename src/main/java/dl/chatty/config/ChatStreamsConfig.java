package dl.chatty.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dl.chatty.chat.repository.ChatRepository;
import dl.chatty.chat.stream.ChatStreams;
import dl.chatty.chat.stream.DefaultChatStreams;
import dl.chatty.security.UsernameEnforcer;
import dl.chatty.security.UsernameSupplier;

@Configuration
public class ChatStreamsConfig {

    @Bean
    public ChatStreams chatStreams(
            ChatRepository chatRepo,
            UsernameSupplier usernameSupplier,
            UsernameEnforcer currentCustomerUsernameEnforcer) {
        return new DefaultChatStreams(chatRepo, usernameSupplier, currentCustomerUsernameEnforcer);
    }
}
