package dl.chatty.concurrency;

import java.util.concurrent.Executor;

/**
 * Provides executors for asynchronous processing
 * 
 * @author Daniel Łaś
 *
 */
public interface ExecutorsProvider {
    /**
     * Messages processing executor
     * 
     * @return
     */
    Executor messageExecutor();
}
