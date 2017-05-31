package dl.chatty.chat.view;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runners.Parameterized.Parameters;

import dl.chatty.DataTestBase;
import dl.chatty.EqualityPair;

public class ChatViewDataTest extends DataTestBase<ChatView> {

    @Parameters
    public static Collection<EqualityPair<ChatView>> parameters() {
        return Arrays.asList(
                EqualityPair.equalPair(
                        new ChatView(null, null, null, null), new ChatView(null, null, null, null)),
                EqualityPair.equalPair(
                        new ChatView("1", null, null, null), new ChatView("1", null, null, null)),
                EqualityPair.inequalPair(
                        new ChatView("1", null, null, null), new ChatView(null, null, null, null)),
                EqualityPair.inequalPair(
                        new ChatView(null, null, null, null), new ChatView("1", null, null, null)),
                EqualityPair.inequalPair(
                        new ChatView(null, null, null, null), "String"));
    }
}
