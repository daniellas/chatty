package dl.chatty.chat.repository;

import java.util.Optional;

public interface ItemFolderProvider {
    Optional<String> getFolder(String item);
}
