package dl.chatty.chat.stream;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

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
        streams.getOne(-1l).blockingFirst();
    }

    @Test
    public void employeeShouldFindAllChats() {
        SecurityTestUtil.setAuthentication(SecurityTestUtil.CUSTOMER_USERNAME, SecurityTestUtil.PASSWORD, Roles.CUSTOMER);

        streams.create(new ChatView(null, "Title 1", null, null)).blockingFirst();
        streams.create(new ChatView(null, "Title 1", null, null)).blockingFirst();

        SecurityTestUtil.setAuthentication(SecurityTestUtil.EMPLOYEE_USERNAME, SecurityTestUtil.PASSWORD, Roles.EMPLOYEE);

        Collection<ChatView> result = streams.findAll().blockingFirst();

        assertThat(result.size(), is(greaterThanOrEqualTo(2)));
    }

    @Test
    public void customerShouldFindOnlyOwnChats() {
        String customer1 = UUID.randomUUID().toString();
        String customer2 = UUID.randomUUID().toString();

        SecurityTestUtil.setAuthentication(customer1, SecurityTestUtil.PASSWORD, Roles.CUSTOMER);

        streams.create(new ChatView(null, "Title 1", null, null)).blockingFirst();
        streams.create(new ChatView(null, "Title 2", null, null)).blockingFirst();

        SecurityTestUtil.setAuthentication(customer2, SecurityTestUtil.PASSWORD, Roles.CUSTOMER);

        streams.create(new ChatView(null, "Title 3", null, null)).blockingFirst();
        streams.create(new ChatView(null, "Title 4", null, null)).blockingFirst();

        SecurityTestUtil.setAuthentication(customer1, SecurityTestUtil.PASSWORD, Roles.CUSTOMER);
        assertEquals(2, streams.findAll().blockingFirst().size());

        SecurityTestUtil.setAuthentication(customer2, SecurityTestUtil.PASSWORD, Roles.CUSTOMER);
        assertEquals(2, streams.findAll().blockingFirst().size());

    }

}
