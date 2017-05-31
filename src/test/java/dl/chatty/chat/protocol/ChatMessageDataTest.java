package dl.chatty.chat.protocol;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runners.Parameterized.Parameters;

import dl.chatty.DataTestBase;
import dl.chatty.EqualityPair;

public class ChatMessageDataTest extends DataTestBase<ChatMessage> {

    @Parameters
    public static Collection<EqualityPair<ChatMessage>> parameters() {
        return Arrays.asList(
                EqualityPair.equalPair(
                        ChatMessage.of(null, null, null, null), ChatMessage.of(null, null, null, null)),
                EqualityPair.equalPair(
                        ChatMessage.of("1", null, null, null), ChatMessage.of("1", null, null, null)),
                EqualityPair.inequalPair(
                        ChatMessage.of("1", null, null, null), ChatMessage.of(null, null, null, null)),
                EqualityPair.inequalPair(
                        ChatMessage.of(null, null, null, null), ChatMessage.of("1", null, null, null)),
                EqualityPair.inequalPair(
                        ChatMessage.of(null, null, null, null), "String"));
    }
}
