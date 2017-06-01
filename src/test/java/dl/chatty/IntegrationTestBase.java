package dl.chatty;

import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ChattyApp.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@Rollback
public abstract class IntegrationTestBase {

    @LocalServerPort
    protected Integer serverPort;

    protected RestTemplate restTemplate = new RestTemplateBuilder().build();

    protected String baseUrl() {
        return "http://localhost:" + serverPort;
    }

}
