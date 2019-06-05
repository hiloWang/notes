package main.study;

import rx.Observable;
import rx.Producer;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 自定义map操作符
 */
public class CustomOperator {

    public static void main(String[] args) {
        Observable.just(32)
                .lift(new MapOperator<>(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        return String.valueOf(integer);
                    }
                }))
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println(s);
                    }
                });
    }

    public static class MapOperator<T, R> implements Observable.Operator<T, R> {

        private Func1<R, T> mMapTransform;

        public MapOperator(Func1<R, T> mapTransform) {
            mMapTransform = mapTransform;
        }

        @Override
        public Subscriber<? super R> call(Subscriber<? super T> subscriber) {
            return new Subscriber<R>() {
                @Override
                public void onCompleted() {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onCompleted();
                        subscriber.setProducer(new Producer() {
                            @Override
                            public void request(long n) {

                            }
                        });
                    }
                }

                @Override
                public void onError(Throwable e) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(e);
                    }
                }

                @Override
                public void onNext(R r) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(mMapTransform.call(r));
                    }
                }
            };
        }
    }


}
