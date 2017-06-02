package dl.chatty.stream;

import org.springframework.web.context.request.async.DeferredResult;

import io.reactivex.Observable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Utility class for {@link Observable} subscriptions
 * 
 * @author Daniel Łaś
 *
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DeferredResultSubscriber {

    /**
     * Subscribes given observable and sets {@link DeferredResult} result in
     * callback
     * 
     * @param observable
     *            to subscribe to
     * 
     * @return {@link DeferredResult} with result or with error in case of error
     */
    public static <T> DeferredResult<T> subscribe(Observable<T> observable) {
        DeferredResult<T> result = new DeferredResult<>();

        observable.subscribe(r -> {
            result.setResult(r);
        }, e -> {
            result.setErrorResult(e);
        });

        return result;
    }

}
