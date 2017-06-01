package dl.chatty.chat.broker;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runners.Parameterized.Parameters;

import dl.chatty.DataTestBase;
import dl.chatty.EqualityPair;
import dl.chatty.chat.broker.DefaultChatSubscriptionRegistry.Subscription;

public class ChatSubscriptionDataTest extends DataTestBase<Subscription> {

    @Parameters
    public static Collection<EqualityPair<Subscription>> parameters() {
        return Arrays.asList(
                EqualityPair.equalPair(
                        Subscription.of(null, null, null),
                        Subscription.of(null, null, null)),
                EqualityPair.equalPair(
                        Subscription.of("id", "user", "sub"),
                        Subscription.of("id", "user", "sub")),
                EqualityPair.inequalPair(
                        Subscription.of("id", "user", "sub"),
                        Subscription.of("id1", "user", "sub")),
                EqualityPair.inequalPair(
                        Subscription.of("id", "user", "sub"),
                        Subscription.of("id", "user1", "sub")),
                EqualityPair.inequalPair(
                        Subscription.of("id", "user", "sub"),
                        Subscription.of("id", "user", "sub1")));
    }
}
