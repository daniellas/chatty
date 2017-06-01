package dl.chatty.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dl.chatty.chat.repository.ChatRepository;
import dl.chatty.chat.stream.ChatStreams;
import dl.chatty.chat.stream.DefaultChatStreams;
import dl.chatty.concurrency.ExecutorsProvider;
import dl.chatty.security.CurrentUsernameSupplier;

@Configuration
public class ChatStreamsConfig {

    @Bean
    public ChatStreams chatStreams(ChatRepository chatRepo, CurrentUsernameSupplier usernameSupplier, ExecutorsProvider executorsProvider) {
        return new DefaultChatStreams(chatRepo, usernameSupplier, executorsProvider);
    }
}
