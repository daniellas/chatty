package dl.chatty.stream;

import org.springframework.web.context.request.async.DeferredResult;

import io.reactivex.Observable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DeferredResultSubscriber {

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
