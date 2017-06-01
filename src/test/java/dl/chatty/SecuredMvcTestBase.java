package dl.chatty;

import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import dl.chatty.config.SecurityConfig;

@RunWith(SpringRunner.class)
@Import({ SecurityConfig.class})
public abstract class SecuredMvcTestBase {
}
