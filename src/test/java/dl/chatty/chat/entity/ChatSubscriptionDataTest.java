package dl.chatty.chat.entity;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runners.Parameterized.Parameters;

import dl.chatty.DataTestBase;
import dl.chatty.EqualityPair;

public class ChatSubscriptionDataTest extends DataTestBase<ChatSubscription> {

    @Parameters
    public static Collection<EqualityPair<ChatSubscription>> parameters() {
        return Arrays.asList(
                EqualityPair.equalPair(
                        ChatSubscription.of(null, null, null, null), ChatSubscription.of(null, null, null, null)),
                EqualityPair.equalPair(
                        ChatSubscription.of(1l, null, null, null), ChatSubscription.of(1l, null, null, null)),
                EqualityPair.inequalPair(
                        ChatSubscription.of(1l, null, null, null), ChatSubscription.of(null, null, null, null)),
                EqualityPair.inequalPair(
                        ChatSubscription.of(null, null, null, null), ChatSubscription.of(1l, null, null, null)),
                EqualityPair.inequalPair(
                        ChatSubscription.of(null, null, null, null), "String"));
    }
}
