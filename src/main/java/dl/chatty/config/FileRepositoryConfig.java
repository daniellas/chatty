package dl.chatty.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dl.chatty.chat.repository.FileChatRepository;
import dl.chatty.chat.repository.FileMessageRepository;
import dl.chatty.chat.repository.ItemFolderProvider;
import dl.chatty.chat.repository.MessageRepository;
import dl.chatty.config.properties.FileRepositoryProperties;
import dl.chatty.datetime.DateTimeSupplier;
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
    public FileChatRepository fileChatRepository(FileRepositoryProperties props, IdSupplier<String> idSupplier) {
        return new FileChatRepository(props.getRootPath(), idSupplier);
    }

    @Bean
    public MessageRepository fileMessageRepository(IdSupplier<String> idSupplier, ItemFolderProvider folderProvider, DateTimeSupplier dateTimeSupplier) {
        return new FileMessageRepository(idSupplier, folderProvider::getFolder, dateTimeSupplier, FileChatRepository.TITLE_FILE_NAME);
    }
}
