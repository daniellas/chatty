package dl.chatty.chat.entity;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runners.Parameterized.Parameters;

import dl.chatty.DataTestBase;
import dl.chatty.EqualityPair;

public class MessageDataTest extends DataTestBase<Message> {

    @Parameters
    public static Collection<EqualityPair<Message>> parameters() {
        return Arrays.asList(
                EqualityPair.equalPair(
                        Message.of(null, null, null, null, null), Message.of(null, null, null, null, null)),
                EqualityPair.equalPair(
                        Message.of(1l, null, null, null, null), Message.of(1l, null, null, null, null)),
                EqualityPair.inequalPair(
                        Message.of(1l, null, null, null, null), Message.of(null, null, null, null, null)),
                EqualityPair.inequalPair(
                        Message.of(null, null, null, null, null), Message.of(1l, null, null, null, null)),
                EqualityPair.inequalPair(
                        Message.of(null, null, null, null, null), "String"));
    }
}
