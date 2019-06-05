package main.study;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author ztiany
 *         Email: ztiany3@gmail.com
 */
public class CustomTransformer {

    private static Executor sExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void main(String... args) {
        Observable.range(1, 30)
                .compose(new MapTransformer())
                .compose(ApplyComputer.applySchedulers())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println(s);
                        System.out.println(Thread.currentThread());
                    }
                });
    }


    public static class MapTransformer implements Observable.Transformer<Integer, String> {

        @Override
        public Observable<String> call(Observable<Integer> integerObservable) {
            return integerObservable
                    .lift(new CustomOperator.MapOperator<String, Integer>(new Func1<Integer, String>() {
                        @Override
                        public String call(Integer integer) {
                            return integer + "dddd";
                        }
                    }));
        }
    }

    public static class ComputerSchedulerTransformer implements Observable.Transformer {

        @Override
        public Object call(Object o) {
            return ((Observable) o).subscribeOn(Schedulers.computation()).observeOn(Schedulers.from(sExecutor));
        }
    }

    public static class ApplyComputer {

        private static ComputerSchedulerTransformer mComputerSchedulerTransformer = new ComputerSchedulerTransformer();

        static <T> Observable.Transformer<T, T> applySchedulers() {
            return (Observable.Transformer<T, T>) mComputerSchedulerTransformer;
        }
    }

}
