package dl.chatty.chat.repository;

import static dl.chatty.file.FileSupport.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.InitializingBean;

import dl.chatty.chat.entity.Chat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class FileChatRepository implements ChatRepository, InitializingBean {

    private static final String CHAT_FOLDER_SEGMENT_SEPARATOR = "_";

    public static final String TITLE_FILE_NAME = "title";

    private final String rootPath;

    private final Supplier<String> chatIdSupplier;

    @Override
    public Optional<Chat> create(Chat sourceChat, String creator) {
        String chatId = chatIdSupplier.get();

        return path(chatFolderPath(chatId, creator))
                .andThen(mkdir())
                .andThen(appendIfPresent(TITLE_FILE_NAME))
                .andThen(mkfileIfPresent())
                .andThen(writeIfPresent(sourceChat.getTitle()))
                .andThen(createChatIfFilesPresent(chatId, sourceChat.getTitle(), creator))
                .apply(rootPath);
    }

    @Override
    public Collection<Chat> findAll(String creator) {
        return filteredStream(optionalFolderCreatorPredicate(creator))
                .sorted(byCreateTs().reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Chat> getOne(String id) {
        return filteredStream(isDirectory().and(nameStartsWith(id)))
                .findFirst();
    }

    private Stream<Chat> filteredStream(Predicate<Path> predicate) {
        return findFiles(predicate)
                .apply(rootPath)
                .parallel()
                .map(SearchItem::of)
                .filter(SearchItem::isValid)
                .map(this::toChat);
    }

    private Predicate<Path> optionalFolderCreatorPredicate(String creator) {
        return Optional.ofNullable(creator)
                .filter(c -> !"".equals(c))
                .map(c -> isDirectory().and(nameEndsWith(CHAT_FOLDER_SEGMENT_SEPARATOR + creator)))
                .orElseGet(() -> isDirectory());
    }

    @Getter
    @RequiredArgsConstructor
    private static class SearchItem {
        private final int segmentsCount;
        private final String chatId;
        private final String creator;
        private final Path path;

        public static SearchItem of(Path path) {
            String[] segments = path.getFileName().toString().split(CHAT_FOLDER_SEGMENT_SEPARATOR);

            return new SearchItem(
                    segments.length,
                    segments.length == 2 ? segments[0] : null,
                    segments.length == 2 ? segments[1] : null,
                    path);
        }

        public static boolean isValid(SearchItem item) {
            return item.getSegmentsCount() == 2;
        }
    }

    private Chat toChat(SearchItem item) {
        String title = append(TITLE_FILE_NAME).andThen(read()).apply(item.getPath()).get();
        Date creationTime = getCreationTime().apply(item.getPath()).get();

        return Chat.of(item.getChatId(), title, item.getCreator(), creationTime);
    }

    private String chatFolderPath(String chatId, String creator) {
        return new StringBuilder(chatId)
                .append(CHAT_FOLDER_SEGMENT_SEPARATOR)
                .append(creator)
                .toString();
    }

    public Optional<String> chatFolderAbsolutePath(String chatId) {
        return getOne(chatId).map(c -> new StringBuilder(rootPath)
                .append("/")
                .append(c.getId())
                .append(CHAT_FOLDER_SEGMENT_SEPARATOR)
                .append(c.getCreatedBy())
                .toString());
    }

    private Comparator<Chat> byCreateTs() {
        return Comparator.comparing(Chat::getCreateTs);
    }

    private Function<Optional<Path>, Optional<Chat>> createChatIfFilesPresent(String chatId, String title, String creator) {
        return titleFile -> titleFile.map(f -> Chat.of(chatId, title, creator, getCreationTime().apply(f).get()));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Path path = mkdir().apply(Paths.get(rootPath)).orElseThrow(() -> {
            return new IllegalStateException("Failed to create root folder: " + rootPath);
        });
        log.info("Created file repository root folder: {}", path.toAbsolutePath());
    }

}
