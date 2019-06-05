package rxlife;

import java.util.concurrent.CancellationException;

import io.reactivex.Completable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import utils.Utils;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2019/2/17 21:45
 */
public class RxLifecycleMain {

    public static void main(String... args) {

        PublishSubject<String> subject = PublishSubject.create();

        Completable.fromAction(() -> Utils.sleep(2000))
                .subscribeOn(Schedulers.computation())
                .compose(upstream -> Completable.ambArray(upstream, subject.flatMapCompletable(CANCEL_COMPLETABLE)))
                .observeOn(Schedulers.newThread())
                .doOnComplete(() -> System.out.println("doOnComplete"))
                .doOnDispose(() -> System.out.println("doOnDispose"))
                .subscribe(
                        () -> {
                            Utils.sleep(1000);
                            System.out.println("-----------------------completed");
                        }, throwable -> {
                            System.out.println("-----------------------throwable: " + throwable);
                        });

        Utils.sleep(1000);

        subject.onNext("");

        Utils.sleep(10000);
    }

    private static final Function<Object, Completable> CANCEL_COMPLETABLE = ignore -> {
        System.out.println("CANCEL_COMPLETABLE");
        return Completable.error(new CancellationException());
    };

}
