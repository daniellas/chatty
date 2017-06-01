package dl.chatty;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runners.Parameterized.Parameters;

import dl.chatty.chat.broker.StompHeadersUtil;
import dl.chatty.chat.mapping.ChatMapper;
import dl.chatty.security.Roles;
import dl.chatty.stream.DeferredResultSubscriber;

public class ConstructorPrivacyTest extends ConstructorPrivacyTestBase {

    @Parameters
    public static Collection<Class<?>> parameters() {
        return Arrays.asList(Roles.class, DeferredResultSubscriber.class, ChatMapper.class, StompHeadersUtil.class);
    }
}
