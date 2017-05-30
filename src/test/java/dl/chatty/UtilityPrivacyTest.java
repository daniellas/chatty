package dl.chatty;

import java.util.Arrays;
import java.util.Collection;

import org.junit.runners.Parameterized.Parameters;

import dl.chatty.file.FileSupport;
import dl.chatty.security.Roles;

public class UtilityPrivacyTest extends ConstructorPrivacyTestBase {

    @Parameters
    public static Collection<Class<?>> parameters() {
        return Arrays.asList(Roles.class, FileSupport.class);
    }
}
