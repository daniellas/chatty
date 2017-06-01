package dl.chatty.chat.broker;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dl.chatty.IntegrationTestBase;
import dl.chatty.chat.repository.ChatSubscriptionRepository;

public class DefaultChatSubscriptionRegistryITest extends IntegrationTestBase {

    private static final String SUB_ID = "sub-0";

    @Autowired
    private ChatSubscriptionRegistry<Long> registry;

    @Autowired
    private ChatSubscriptionRepository chatSubsriptionRepository;

    @Test
    public void shouldReturnChatUserDestination() {
        assertThat(registry.chatUserDestination(1l, "user1"), is("/1/user1"));
    }

    @Test
    public void shouldReturnChatDestinations() {
        fillData();
        assertThat(registry.chatDestinations(1l), containsInAnyOrder("/1/user1"));
    }

    @Test
    public void shouldReturnEmptyChatDestinations() {
        fillData();
        assertThat(registry.chatDestinations(5l), is(empty()));
    }

    @Test
    public void shouldReturnChatDestinationsAfterRemoveSubscription() {
        fillData();
        registry.remove("user1", Collections.singletonList(SUB_ID));

        assertThat(registry.chatDestinations(2l), containsInAnyOrder("/2/user2"));
        assertThat(registry.chatDestinations(4l), containsInAnyOrder("/4/user2"));
    }

    @Test
    public void shouldReturnChatDestinationsAfterRemoveUser() {
        fillData();
        registry.remove("user1");

        assertThat(registry.chatDestinations(2l), containsInAnyOrder("/2/user2"));
        assertThat(registry.chatDestinations(4l), containsInAnyOrder("/4/user2"));
    }

    private void fillData() {
        chatSubsriptionRepository.deleteAll();
        registry.create(1l, "user1", Arrays.asList(SUB_ID));
        registry.create(2l, "user2", Arrays.asList(SUB_ID));
        registry.create(3l, "user1", Arrays.asList(SUB_ID));
        registry.create(4l, "user2", Arrays.asList(SUB_ID));
    }

}
