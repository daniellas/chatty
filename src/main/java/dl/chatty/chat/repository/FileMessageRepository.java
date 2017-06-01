package dl.chatty.chat.repository;

import static dl.chatty.file.FileSupport.*;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import dl.chatty.chat.entity.Message;
import dl.chatty.datetime.DateTimeSupplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FileMessageRepository implements MessageRepository {

    private static final String MESSAGE_FILE_SEGMENT_SEPARATOR = "_";

    private final Supplier<String> messageIdSupplier;

    private final Function<String, Optional<String>> chatFolderSupplier;

    private final DateTimeSupplier dateTimeSupplier;

    private final String chatTitleFilename;

    @Override
    public Optional<Message> create(String chatId, Message sourceMessage, String sender) {
        String messageId = messageIdSupplier.get();
        long timestamp = dateTimeSupplier.get().getTime();

        return chatFolderSupplier.apply(chatId)
                .flatMap(chatFolder -> {
                    return path(messageFilePath(messageId, sender, timestamp))
                            .andThen(mkfile())
                            .andThen(writeIfPresent(sourceMessage.getMessage()))
                            .andThen(createMessageIfFilesPResent(messageId, sourceMessage, sender))
                            .apply(chatFolder);
                });
    }

    @Override
    public Collection<Message> findForChat(String chatId) {
        return chatFolderSupplier.apply(chatId)
                .map(chatFolder -> {
                    return findFiles(isFile().and(nameIs(chatTitleFilename).negate())).apply(chatFolder)
                            .parallel()
                            .map(SearchItem::of)
                            .filter(SearchItem::isValid)
                            .map(this::toMessage)
                            .sorted(bySentTs())
                            .collect(Collectors.toList());
                })
                .orElseGet(() -> Collections.emptyList());
    }

    private Message toMessage(SearchItem item) {
        return Message.of(
                item.getMessageId(),
                read().apply(item.getPath()).get(),
                item.getSender(),
                new Date(Long.parseLong(item.getTimestamp())));
    }

    @Getter
    @RequiredArgsConstructor
    static class SearchItem {
        private static final int VALID_SEGMENTS_COUNT = 3;
        private final int segmentsCount;
        private final String messageId;
        private final String timestamp;
        private final String sender;
        private final Path path;

        public static SearchItem of(Path path) {
            String[] segments = path.getFileName().toString().split(MESSAGE_FILE_SEGMENT_SEPARATOR);
            boolean isValid = segments.length == VALID_SEGMENTS_COUNT;

            return new SearchItem(
                    segments.length,
                    isValid ? segments[0] : null,
                    isValid ? segments[1] : null,
                    isValid ? segments[2] : null,
                    path);
        }

        public static boolean isValid(SearchItem item) {
            return item.getSegmentsCount() == VALID_SEGMENTS_COUNT && isTimestampValid(item.getTimestamp());
        }

        private static boolean isTimestampValid(String timestamp) {
            try {
                return Long.parseLong(timestamp) > 0;
            } catch (NumberFormatException e) {
                return false;
            }

        }
    }

    private static String messageFilePath(String messageId, String sender, long timestamp) {
        return new StringBuilder(messageId)
                .append(MESSAGE_FILE_SEGMENT_SEPARATOR)
                .append(timestamp)
                .append(MESSAGE_FILE_SEGMENT_SEPARATOR)
                .append(sender)
                .toString();
    }

    private Function<Optional<Path>, Optional<Message>> createMessageIfFilesPResent(String messageId, Message sourceMessage, String sender) {
        return messageFile -> messageFile.map(f -> Message.of(
                messageId,
                sourceMessage.getMessage(),
                sender,
                getCreationTime().apply(f).get()));
    }

    private Comparator<Message> bySentTs() {
        return Comparator.comparing(Message::getSentTs);
    }
}
