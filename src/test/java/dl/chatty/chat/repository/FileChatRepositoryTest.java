package dl.chatty.chat.repository;

import static dl.chatty.FileTestUtil.*;
import static dl.chatty.file.FileSupport.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Optional;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import dl.chatty.FileTestUtil;
import dl.chatty.chat.entity.Chat;

public class FileChatRepositoryTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    private static final Chat CHAT = Chat.of(null, "Chat title", null, null);

    private static final String CHAT_ID = "id";

    private static final String CHAT_CREATOR = "customer";

    private static final String SEGMENT_SEPARATOR = "_";

    private FileChatRepository repo;

    @Before
    public void before() {
        repo = new FileChatRepository(tmp.getRoot().getAbsolutePath(), () -> CHAT_ID);
    }

    @Test
    public void shouldCreateChatFolder() {
        Optional<Chat> chat = repo.create(CHAT, CHAT_CREATOR);

        assertTrue(chat.isPresent());
        assertFolderExists(tmp.getRoot().getAbsolutePath() + "/" + expectedChatFolderName());
    }

    @Test
    public void shouldCreateChatTitleFile() {
        Optional<Chat> chat = repo.create(CHAT, CHAT_CREATOR);

        assertTrue(chat.isPresent());
        assertFileExists(tmp.getRoot().getAbsolutePath() + "/" + expectedChatFolderName() + "/title");
    }

    @Test
    public void shouldGetOne() {
        Optional<Chat> chat = repo.create(CHAT, CHAT_CREATOR);
        Optional<Chat> result = repo.getOne(CHAT_ID);

        assertEquals(chat, result);
    }

    @Test
    public void shouldFindAllOnNullCreatorParam() {
        Optional<Chat> chat1 = repo.create(CHAT, CHAT_CREATOR);
        Optional<Chat> chat2 = repo.create(CHAT, "jan");
        Collection<Chat> result = repo.findAll(null);

        assertThat(result, containsInAnyOrder(chat1.get(), chat2.get()));
    }

    @Test
    public void shouldFindAllOnEmptyCreatorParam() {
        Optional<Chat> chat1 = repo.create(CHAT, CHAT_CREATOR);
        Optional<Chat> chat2 = repo.create(CHAT, "jan");
        Collection<Chat> result = repo.findAll("");

        assertThat(result, containsInAnyOrder(chat1.get(), chat2.get()));
    }

    @Test
    public void shouldFindAllOrdered() throws InterruptedException {
        Optional<Chat> chat1 = repo.create(CHAT, CHAT_CREATOR);
        Thread.sleep(1000);
        Optional<Chat> chat2 = repo.create(CHAT, "jan");
        Collection<Chat> result = repo.findAll(null);

        assertThat(result, contains(chat1.get(), chat2.get()));
    }

    @Test
    public void shouldFindFiltered() {
        Optional<Chat> chat1 = repo.create(CHAT, CHAT_CREATOR);

        repo.create(CHAT, "jan");

        Collection<Chat> result = repo.findAll(CHAT_CREATOR);

        assertThat(result, contains(chat1.get()));
    }

    @Test
    public void shouldSkipNotMatchingFolders() {
        path("id1").andThen(mkdir())
                .apply(FileTestUtil.absolutePath(tmp.getRoot()));

        Optional<Chat> chat1 = repo.create(CHAT, CHAT_CREATOR);
        Optional<Chat> chat2 = repo.create(CHAT, "jan");
        Collection<Chat> result = repo.findAll(null);

        assertThat(result, containsInAnyOrder(chat1.get(), chat2.get()));
    }

    @Test
    public void shouldReturnChatFolderPath() {
        Optional<Chat> chat = repo.create(CHAT, CHAT_CREATOR);

        assertEquals(
                absolutePath(tmp.getRoot()) + "/" + expectedChatFolderName(),
                repo.getFolder(chat.get().getId()).get());
    }

    private String expectedChatFolderName() {
        return CHAT_ID + SEGMENT_SEPARATOR + CHAT_CREATOR;
    }

}
