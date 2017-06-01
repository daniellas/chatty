package dl.chatty.chat.broker;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

public class DefaultChatSubscriptionRegistryTest {

    private static final String SUB_ID = "sub-0";
    
    private ChatSubscriptionRegistry<String> registry;

    @Before
    public void before() {
        registry = new DefaultChatSubscriptionRegistry(null);

        registry.create("chat1", "user1", Arrays.asList(SUB_ID));
        registry.create("chat1", "user2", Arrays.asList(SUB_ID));
        registry.create("chat2", "user1", Arrays.asList(SUB_ID));
        registry.create("chat2", "user2", Arrays.asList(SUB_ID));
    }

    @Test
    public void shouldReturnChatChatUserDestination() {
        assertThat(registry.chatUserDestination("chat1", "user1"), is("/chat1/user1"));
    }

    @Test
    public void shouldReturnChatDestinations() {
        assertThat(registry.chatDestinations("chat1"), containsInAnyOrder("/chat1/user1", "/chat1/user2"));
    }

    @Test
    public void shouldReturnEmptyChatDestinations() {
        assertThat(registry.chatDestinations("chat3"), is(empty()));
    }

    @Test
    public void shouldReturnChatDestinationsAfterRemoveSubscription() {
        registry.remove("user1", Collections.singletonList(SUB_ID));

        assertThat(registry.chatDestinations("chat1"), containsInAnyOrder("/chat1/user2"));
        assertThat(registry.chatDestinations("chat2"), containsInAnyOrder("/chat2/user2"));
    }

    @Test
    public void shouldReturnChatDestinationsAfterRemoveUser() {
        registry.remove("user1");

        assertThat(registry.chatDestinations("chat1"), containsInAnyOrder("/chat1/user2"));
        assertThat(registry.chatDestinations("chat2"), containsInAnyOrder("/chat2/user2"));
    }

}
