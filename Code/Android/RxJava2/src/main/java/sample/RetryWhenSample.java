package sample;

import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import utils.Utils;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/12/20 20:09
 */
public class RetryWhenSample {

    private static int count = 0;

    public static void main(String... args) {
        Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("call count = " + count++);
                if (count < 3) {
                    throw new NullPointerException("mock error");
                } else {
                    return "111111111111";
                }
            }
        }).retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                System.out.println("RetryWhenSample.apply");
                return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Throwable throwable) throws Exception {
                        if (throwable instanceof NullPointerException) {
                            //return Observable.timer(1000, TimeUnit.MILLISECONDS);
                            return Completable
                                    .complete()
                                    .toObservable()
                                    .defaultIfEmpty(1);

                        } else {
                            return Observable.error(throwable);
                        }
                    }
                });
            }
        }).subscribe(
                s -> {
                    System.out.print("RetryWhenSample.success.accept");
                    System.out.println("s = [" + s + "]");
                },
                throwable -> {
                    System.out.print("RetryWhenSample.error.accept");
                    System.out.println("throwable = [" + throwable + "]");
                }
        );

        Utils.sleep(10000);
    }

}
