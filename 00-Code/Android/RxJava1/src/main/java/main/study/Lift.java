package main.study;

import rx.Observable;
import rx.Subscriber;

/**
 * 变换原理
 */
public class Lift {

    private static class DemoCompose implements Observable.Transformer<Integer, Integer> {

        @Override
        public Observable<Integer> call(Observable<Integer> integerObservable) {
            return integerObservable
                    .lift(new Operator1())
                    .lift(new Operator2())
                    .lift(new Operator3())
                    .lift(new Operator4());
        }
    }

    private static class Operator1 implements Observable.Operator<String, Integer> {
        @Override
        public Subscriber<? super Integer> call(Subscriber<? super String> subscriber) {
            return null;
        }
    }

    private static class Operator2 implements Observable.Operator<Integer, String> {
        @Override
        public Subscriber<? super String> call(Subscriber<? super Integer> subscriber) {
            return null;
        }
    }


    private static class Operator3 implements Observable.Operator<String, Integer> {
        @Override
        public Subscriber<? super Integer> call(Subscriber<? super String> subscriber) {
            return null;
        }
    }

    private static class Operator4 implements Observable.Operator<Integer, String> {
        @Override
        public Subscriber<? super String> call(Subscriber<? super Integer> subscriber) {
            return null;
        }
    }

}
