package dl.chatty.stream;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.context.request.async.DeferredResult;

import io.reactivex.Observable;

public class DeferredResultSubscriberTest {

    @Test
    public void shouldSetResult() {
        DeferredResult<String> result = DeferredResultSubscriber.subscribe(Observable.just("a"));

        Assert.assertEquals("a", result.getResult());
    }

    @Test
    public void shouldSetError() {
        RuntimeException ex = new RuntimeException();

        Observable<Object> observable = Observable.just("a").map(a -> {
            throw ex;
        });

        DeferredResult<?> result = DeferredResultSubscriber.subscribe(observable);

        Assert.assertEquals(ex, result.getResult());
    }

}
