package dl.chatty.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "chatty.repository.file")
public class FileRepositoryProperties {
    private String rootPath;
}
