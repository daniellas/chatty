package dl.chatty.concurrency;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource
public class ConfigurableExecutorsProvider implements ExecutorsProvider {

    private final ThreadPoolExecutor messageExecutor = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors(),
            100L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());

    @Override
    public Executor messageExecutor() {
        return messageExecutor;
    }

    @ManagedAttribute
    public int getMessagePoolSize() {
        return messageExecutor.getPoolSize();
    }

    @ManagedAttribute
    public int getMessageMaximumPoolSize() {
        return messageExecutor.getMaximumPoolSize();
    }

    @ManagedAttribute
    public int getMessageCorePoolSize() {
        return messageExecutor.getCorePoolSize();
    }

    @ManagedAttribute
    public long getMessageKeepAliveTimeMs() {
        return messageExecutor.getKeepAliveTime(TimeUnit.MILLISECONDS);
    }

    @ManagedOperation
    public void setMessageProperties(int corePoolSize, int maximumPoolSize, long keepAliveTimeMs) {
        messageExecutor.setCorePoolSize(corePoolSize);
        messageExecutor.setMaximumPoolSize(maximumPoolSize);
        messageExecutor.setKeepAliveTime(keepAliveTimeMs, TimeUnit.MILLISECONDS);
    }

}
