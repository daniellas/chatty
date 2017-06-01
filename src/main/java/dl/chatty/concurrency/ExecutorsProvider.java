package dl.chatty.concurrency;

import java.util.concurrent.Executor;

public interface ExecutorsProvider {
    Executor fileIOExecutor();
    Executor messageExecutor();
}
