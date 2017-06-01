package dl.chatty.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dl.chatty.chat.repository.ChatRepository;
import dl.chatty.chat.stream.ChatStreams;
import dl.chatty.chat.stream.DefaultChatStreams;
import dl.chatty.concurrency.ExecutorsProvider;
import dl.chatty.security.UsernameSupplier;
import dl.chatty.security.UsernameEnforcer;

@Configuration
public class ChatStreamsConfig {

    @Bean
    public ChatStreams chatStreams(
            ChatRepository chatRepo,
            UsernameSupplier usernameSupplier,
            ExecutorsProvider executorsProvider,
            UsernameEnforcer currentCustomerUsernameEnforcer) {
        return new DefaultChatStreams(chatRepo, usernameSupplier, executorsProvider, currentCustomerUsernameEnforcer);
    }
}
