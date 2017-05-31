package dl.chatty.chat.entity;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runners.Parameterized.Parameters;

import dl.chatty.DataTestBase;
import dl.chatty.EqualityPair;

public class ChatDataTest extends DataTestBase<Chat> {

    @Parameters
    public static Collection<EqualityPair<Chat>> parameters() {
        return Arrays.asList(
                EqualityPair.equalPair(
                        Chat.of(null, null, null, null), Chat.of(null, null, null, null)),
                EqualityPair.equalPair(
                        Chat.of("1", null, null, null), Chat.of("1", null, null, null)),
                EqualityPair.inequalPair(
                        Chat.of("1", null, null, null), Chat.of(null, null, null, null)),
                EqualityPair.inequalPair(
                        Chat.of(null, null, null, null), Chat.of("1", null, null, null)),
                EqualityPair.inequalPair(
                        Chat.of(null, null, null, null), "String"));
    }
}
