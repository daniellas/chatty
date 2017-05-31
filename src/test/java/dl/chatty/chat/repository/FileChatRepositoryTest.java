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

    private FileChatRepository repo;

    private static final Chat CHAT = Chat.of(null, "Chat title", null, null);

    @Before
    public void before() {
        repo = new FileChatRepository(tmp.getRoot().getAbsolutePath(), () -> "id");
    }

    @Test
    public void shouldCreateChatFolder() {
        Optional<Chat> chat = repo.create(CHAT, "daniel");

        assertTrue(chat.isPresent());
        assertFolderExists(tmp.getRoot().getAbsolutePath() + "/id_daniel");
    }

    @Test
    public void shouldCreateChatTitleFile() {
        Optional<Chat> chat = repo.create(CHAT, "daniel");

        assertTrue(chat.isPresent());
        assertFileExists(tmp.getRoot().getAbsolutePath() + "/id_daniel/title");
    }

    @Test
    public void shouldGetOne() {
        Optional<Chat> chat = repo.create(CHAT, "daniel");
        Optional<Chat> result = repo.getOne("id");

        assertEquals(chat, result);
    }

    @Test
    public void shouldFindAllOnNullCreatorParam() {
        Optional<Chat> chat1 = repo.create(CHAT, "daniel");
        Optional<Chat> chat2 = repo.create(CHAT, "jan");
        Collection<Chat> result = repo.findAll(null);

        assertThat(result, containsInAnyOrder(chat1.get(), chat2.get()));
    }

    @Test
    public void shouldFindAllOnEmptyCreatorParam() {
        Optional<Chat> chat1 = repo.create(CHAT, "daniel");
        Optional<Chat> chat2 = repo.create(CHAT, "jan");
        Collection<Chat> result = repo.findAll("");

        assertThat(result, containsInAnyOrder(chat1.get(), chat2.get()));
    }

    @Test
    public void shouldFindAllOrdered() throws InterruptedException {
        Optional<Chat> chat1 = repo.create(CHAT, "daniel");
        Thread.sleep(1000);
        Optional<Chat> chat2 = repo.create(CHAT, "jan");
        Collection<Chat> result = repo.findAll(null);

        assertThat(result, contains(chat1.get(), chat2.get()));
    }

    @Test
    public void shouldFindFiltered() {
        Optional<Chat> chat1 = repo.create(CHAT, "daniel");

        repo.create(CHAT, "jan");

        Collection<Chat> result = repo.findAll("daniel");

        assertThat(result, contains(chat1.get()));
    }

    @Test
    public void shouldSkipNotMatchingFolders() {
        path("id1").andThen(mkdir())
                .apply(FileTestUtil.absolutePath(tmp.getRoot()));

        Optional<Chat> chat1 = repo.create(CHAT, "daniel");
        Optional<Chat> chat2 = repo.create(CHAT, "jan");
        Collection<Chat> result = repo.findAll(null);

        assertThat(result, containsInAnyOrder(chat1.get(), chat2.get()));
    }

}
