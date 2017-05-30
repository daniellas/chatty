package dl.chatty.chat.repository;

import static dl.chatty.file.FileSupport.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.InitializingBean;

import dl.chatty.chat.entity.Chat;
import dl.chatty.id.IdSupplier;
import io.atlassian.fugue.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class FileChatRepository implements ChatRepository, InitializingBean {

    private static final String CHAT_FOLDER_SEGMENT_SEPARATOR = "_";

    private static final String TITLE_FILE_NAME = "title";

    private final String rootPath;

    private final IdSupplier<String> idSupplier;

    @Override
    public Optional<Chat> create(Chat chat, String creator) {
        String id = idSupplier.get();

        return path(chatFolder(id, creator))
                .andThen(mkdir())
                .apply(rootPath)
                .flatMap(dir -> {
                    return append(TITLE_FILE_NAME)
                            .andThen(mkfile())
                            .andThen(flatWrite(chat.getTitle()))
                            .apply(dir)
                            .map(t -> {
                                return Chat.of(
                                        id,
                                        chat.getTitle(),
                                        creator,
                                        creationTime().apply(dir).get());
                            });
                });
    }

    @Override
    public Collection<Chat> findAll(String creator) {
        return filteredStream(optionalFolderCreatorPredicate(creator))
                .sorted(byCreateTs())
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
                .map(this::toSearchPair)
                .filter(this::hasTwoSegments)
                .map(this::toChat);
    }

    private Predicate<Path> optionalFolderCreatorPredicate(String creator) {
        return Optional.ofNullable(creator)
                .filter(c -> !"".equals(c))
                .map(c -> {
                    return isDirectory().and(nameEnsdWith(CHAT_FOLDER_SEGMENT_SEPARATOR + creator));
                })
                .orElseGet(() -> isDirectory());
    }

    private Pair<String[], Path> toSearchPair(Path path) {
        return Pair.pair(path.getFileName().toString().split(CHAT_FOLDER_SEGMENT_SEPARATOR), path);
    }

    private boolean hasTwoSegments(Pair<String[], Path> pair) {
        return pair.left().length == 2;
    }

    private Chat toChat(Pair<String[], Path> pair) {
        String title = append(TITLE_FILE_NAME)
                .andThen(read())
                .apply(pair.right())
                .orElse("Missing chat title");
        // TODO What to do here ?
        Date creationTime = creationTime().apply(pair.right()).orElse(null);

        return Chat.of(pair.left()[0], title, pair.left()[1], creationTime);
    }

    public static String chatFolder(String chatId, String creator) {
        return new StringBuilder(chatId)
                .append(CHAT_FOLDER_SEGMENT_SEPARATOR)
                .append(creator)
                .toString();
    }

    private Comparator<Chat> byCreateTs() {
        return Comparator.comparing(Chat::getCreateTs);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Path path = mkdir().apply(Paths.get(rootPath)).orElseThrow(() -> {
            return new IllegalStateException("Failed to create root folder: " + rootPath);
        });
        log.info("Created file repository root folder: {}", path.toAbsolutePath());
    }

}
