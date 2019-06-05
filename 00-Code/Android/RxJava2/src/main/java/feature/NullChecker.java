package feature;


import io.reactivex.Flowable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * RxJava的流中不允许传递空值
 *
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class NullChecker {

    public static void main(String... args) {
        justNull();
    }

    private static void justNull() {
        Flowable.just(null);
        io.reactivex.Observable.just(1)
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Integer integer) throws Exception {
                        return null;
                    }
                })

                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        System.out.println(s);
                    }
                });
        System.out.println("NullChecker.justNull");
    }

}
