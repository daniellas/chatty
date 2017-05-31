package dl.chatty.chat.repository;

import static dl.chatty.FileTestUtil.*;
import static dl.chatty.file.FileSupport.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import dl.chatty.chat.entity.Message;

public class FileMessageRepositoryTest {

    private static final String CHAT_ID = "chatid";

    private static final String CHAT_CREATOR = "customer";

    private static final String MESSAGE_SENDER = "customer";

    private static final String MESSAGE_ID = "messageid";

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    private FileMessageRepository repo;

    @Before
    public void before() {
        repo = new FileMessageRepository(this::messageIdSupplier, this::chatFolderSupplier, FileChatRepository.TITLE_FILE_NAME);

        path(CHAT_ID + "_" + CHAT_CREATOR).andThen(mkdir())
                .andThen(appendIfPresent(FileChatRepository.TITLE_FILE_NAME))
                .andThen(mkfileIfPresent())
                .andThen(writeIfPresent("Chat title"))
                .apply(absolutePath(tmp.getRoot()));
    }

    @Test
    public void shouldCreateMessage() {
        Optional<Message> message = repo.create(CHAT_ID, Message.of(null, "Chat message", null, null), MESSAGE_SENDER);

        assertTrue(message.isPresent());
    }

    @Test
    public void shouldSetMessageId() {
        Optional<Message> message = repo.create(CHAT_ID, Message.of(null, "Chat message", null, null), MESSAGE_SENDER);

        assertEquals(MESSAGE_ID, message.get().getId());
    }

    @Test
    public void shouldSetMessageSender() {
        Optional<Message> message = repo.create(CHAT_ID, Message.of(null, "Chat message", null, null), MESSAGE_SENDER);

        assertEquals(MESSAGE_SENDER, message.get().getFrom());
    }

    @Test
    public void shouldSetMessageContent() {
        Optional<Message> message = repo.create(CHAT_ID, Message.of(null, "Chat message", null, null), MESSAGE_SENDER);

        assertEquals("Chat message", message.get().getMessage());
    }

    @Test
    public void shouldSetMessageSentTs() {
        Optional<Message> message = repo.create(CHAT_ID, Message.of(null, "Chat message", null, null), MESSAGE_SENDER);

        assertNotNull(message.get().getSentTs());
    }

    @Test
    public void shouldCreateMessageFile() {
        repo.create(CHAT_ID, Message.of(null, "Chat message", null, null), MESSAGE_SENDER);

        assertFileExists(messageFilepath());
    }

    @Test
    public void shouldWriteMessageContentToFile() {
        repo.create(CHAT_ID, Message.of(null, "Chat message", null, null), MESSAGE_SENDER);

        Optional<String> message = read().apply(Paths.get(messageFilepath()));

        assertEquals("Chat message", message.get());
    }

    @Test
    public void shouldFindAllMessages() {
        AtomicLong idGenerator = new AtomicLong();
        FileMessageRepository idGeneratingRepo = new FileMessageRepository(
                () -> {
                    return Long.valueOf(idGenerator.incrementAndGet()).toString();
                },
                this::chatFolderSupplier,
                FileChatRepository.TITLE_FILE_NAME);

        idGeneratingRepo.create(CHAT_ID, Message.of(null, "Chat message1", null, null), "customer");
        idGeneratingRepo.create(CHAT_ID, Message.of(null, "Chat message2", null, null), "employee");

        Collection<Message> messages = idGeneratingRepo.findForChat(CHAT_ID);

        assertThat(messages, containsInAnyOrder(
                Message.of("1", "Chat message1", null, null),
                Message.of("2", "Chat message2", null, null)));
    }

    private String messageFilepath() {
        return absolutePath(tmp.getRoot()) + "/" + CHAT_ID + "_" + CHAT_CREATOR + "/" + MESSAGE_ID + "_" + MESSAGE_SENDER;
    }

    private String messageIdSupplier() {
        return MESSAGE_ID;
    }

    private Optional<String> chatFolderSupplier(String chatId) {
        return Optional.of(absolutePath(tmp.getRoot()) + "/" + chatId + "_" + CHAT_CREATOR);
    }
}
