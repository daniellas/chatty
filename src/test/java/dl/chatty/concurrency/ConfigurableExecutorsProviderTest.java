package dl.chatty.concurrency;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Test;

public class ConfigurableExecutorsProviderTest {

    private ConfigurableExecutorsProvider provider;

    @Before
    public void before() {
        provider = new ConfigurableExecutorsProvider();
    }

    @Test
    public void shouldReturnMesagesExecutor() {
        assertNotNull(provider.messageExecutor());
    }

    @Test
    public void shouldSetMessageIOProperties() {
        provider.setMessageProperties(10, 20, 100);

        assertEquals(10, provider.getMessageCorePoolSize());
        assertEquals(20, provider.getMessageMaximumPoolSize());
        assertEquals(100, provider.getMessageKeepAliveTimeMs());
    }

    @Test
    public void shouldReturnMessagePoolSizeWithRunningTask() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        provider.messageExecutor().execute(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
            }
        });
        assertEquals(1, provider.getMessagePoolSize());
        latch.countDown();
    }

}
