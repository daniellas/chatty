package dl.chatty.chat.broker;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import dl.chatty.IntegrationTestBase;
import dl.chatty.chat.broker.DefaultChatSubscriptionRegistry.RegistryChatSubscription;
import dl.chatty.chat.repository.ChatSubscriptionRepository;

public class DefaultChatSubscriptionRegistryITest extends IntegrationTestBase {

    private static final String SESSION_ID = "sid";

    @Autowired
    private ChatSubscriptionRegistry<Long> registry;

    @Autowired
    private ChatSubscriptionRepository chatSubsriptionRepository;

    @Test
    public void shouldReturnChatSubscription() {
        fillTestData();
        assertThat(registry.chatSubscriptions(1l), containsInAnyOrder(
                RegistryChatSubscription.of("user1", "sid"),
                RegistryChatSubscription.of("user2", "sid")));
    }

    @Test
    public void shouldReturnEmptyChatDestinations() {
        fillTestData();
        assertThat(registry.chatSubscriptions(5l), is(empty()));
    }

    @Test
    public void shouldReturnChatDestinationsAfterRemoveSubscription() {
        fillTestData();
        registry.remove("user1", SESSION_ID);

        assertThat(registry.chatSubscriptions(1l), containsInAnyOrder(RegistryChatSubscription.of("user2", "sid")));
        assertThat(registry.chatSubscriptions(2l), containsInAnyOrder(RegistryChatSubscription.of("user2", "sid")));
    }

    @Test
    public void shouldReturnChatDestinationsAfterRemoveUser() {
        fillTestData();
        registry.remove("user1");

        assertThat(registry.chatSubscriptions(1l), containsInAnyOrder(RegistryChatSubscription.of("user2", "sid")));
        assertThat(registry.chatSubscriptions(2l), containsInAnyOrder(RegistryChatSubscription.of("user2", "sid")));
    }

    @Test
    public void shouldNotFailOnDuplicateRegistration() {
        fillTestData();

        registry.create(1l, "user1", SESSION_ID);
    }

    private void fillTestData() {
        chatSubsriptionRepository.deleteAll();
        registry.create(1l, "user1", SESSION_ID);
        registry.create(1l, "user2", SESSION_ID);
        registry.create(2l, "user1", SESSION_ID);
        registry.create(2l, "user2", SESSION_ID);
    }

}
