package dl.chatty.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dl.chatty.chat.repository.ChatRepository;
import dl.chatty.chat.repository.FileChatRepository;
import dl.chatty.config.properties.FileRepositoryProperties;
import dl.chatty.id.IdSupplier;
import dl.chatty.id.UUIDIdSupplier;

@Configuration
@EnableConfigurationProperties(FileRepositoryProperties.class)
public class FileRepositoryConfig {

    @Bean
    public IdSupplier<String> uuiIdSupplier() {
        return new UUIDIdSupplier();
    }

    @Bean
    public ChatRepository fileChatRepository(FileRepositoryProperties props, IdSupplier<String> idSupplier) {
        return new FileChatRepository(props.getRootPath(), idSupplier);
    }
}
