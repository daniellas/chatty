package dl.chatty.chat.stream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.Assert.*;

import static org.hamcrest.Matchers.*;

import java.util.Collection;
import java.util.NoSuchElementException;

import dl.chatty.IntegrationTestBase;
import dl.chatty.SecurityTestUtil;
import dl.chatty.chat.view.ChatView;
import dl.chatty.security.Roles;

public class DefaultChatStreamsITest extends IntegrationTestBase {

    @Autowired
    private ChatStreams streams;

    @Before
    public void before() {
        SecurityTestUtil.clearAuthentication();
    }

    @Test(expected = AccessDeniedException.class)
    public void chatCreationByEmployeeShouldBeForbidden() {
        SecurityTestUtil.setAuthentication(SecurityTestUtil.EMPLOYEE_USERNAME, SecurityTestUtil.PASSWORD, Roles.EMPLOYEE);
        streams.create(new ChatView(null, null, null, null));
    }

    @Test
    public void customerShouldCreateChat() {
        SecurityTestUtil.setAuthentication(SecurityTestUtil.CUSTOMER_USERNAME, SecurityTestUtil.PASSWORD, Roles.CUSTOMER);

        ChatView result = streams.create(new ChatView(null, "Title", null, null)).blockingFirst();

        assertEquals(SecurityTestUtil.CUSTOMER_USERNAME, result.getCreatedBy());
        assertEquals("Title", result.getTitle());
        assertNotNull(result.getId());
        assertNotNull(result.getCreateTs());
    }

    @Test
    public void shouldFindAllChats() {
        SecurityTestUtil.setAuthentication(SecurityTestUtil.CUSTOMER_USERNAME, SecurityTestUtil.PASSWORD, Roles.CUSTOMER);

        streams.create(new ChatView(null, "Title 1", null, null)).blockingFirst();
        streams.create(new ChatView(null, "Title 1", null, null)).blockingFirst();

        Collection<ChatView> result = streams.findAll().blockingFirst();

        assertThat(result.size(), is(greaterThanOrEqualTo(2)));
    }

    @Test
    public void shouldGetOneChat() {
        SecurityTestUtil.setAuthentication(SecurityTestUtil.CUSTOMER_USERNAME, SecurityTestUtil.PASSWORD, Roles.CUSTOMER);
        ChatView chat = streams.create(new ChatView(null, "Title 1", null, null)).blockingFirst();
        ChatView result = streams.getOne(chat.getId()).blockingFirst();

        assertEquals(chat.getId(), result.getId());
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldFailOnNonExistentChatGet() {
        streams.getOne("x").blockingFirst();
    }
}
