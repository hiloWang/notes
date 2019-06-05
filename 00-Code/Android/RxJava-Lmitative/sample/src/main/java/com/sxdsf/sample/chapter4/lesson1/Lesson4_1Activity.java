package com.sxdsf.sample.chapter4.lesson1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sxdsf.sample.R;


public class Lesson4_1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson4_1);
        findViewById(R.id.btnRxJava1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Observable.
//                        create(new Observable.OnSubscribe<String>() {
//                            @Override
//                            public void call(Subscriber<? super String> subscriber) {
//                                if (!subscriber.isUnsubscribed()) {
//                                    subscriber.onNext("1");
//                                    subscriber.onNext("2");
//                                    subscriber.onCompleted();
//                                }
//                            }
//                        }).
//                        map(new Func1<String, Integer>() {
//                            @Override
//                            public Integer call(String s) {
//                                return Integer.parseInt(s);
//                            }
//                        }).
//                        subscribe(new Observer<Integer>() {
//                            @Override
//                            public void onCompleted() {
//                                System.out.println("onCompleted");
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//
//                            }
//
//                            @Override
//                            public void onNext(Integer integer) {
//                                System.out.println("onNext:" + integer + ",integer instanceOf" + integer.getClass());
//                            }
//                        });
            }
        });

        findViewById(R.id.btnRxJava2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Observable.
//                        create(new ObservableOnSubscribe<String>() {
//                            @Override
//                            public void subscribe(ObservableEmitter<String> e) throws Exception {
//                                if (!e.isDisposed()) {
//                                    e.onNext("1");
//                                    e.onNext("2");
//                                    e.onComplete();
//                                }
//                            }
//                        }).
//                        map(new Function<String, Integer>() {
//                            @Override
//                            public Integer apply(String s) throws Exception {
//                                return Integer.parseInt(s);
//                            }
//                        }).
//                        subscribe(new Observer<Integer>() {
//                            @Override
//                            public void onSubscribe(Disposable d) {
//                                System.out.println("onSubscribe");
//                            }
//
//                            @Override
//                            public void onNext(Integer value) {
//                                System.out.println("onNext:" + value);
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//
//                            }
//
//                            @Override
//                            public void onComplete() {
//                                System.out.println("onComplete");
//                            }
//                        });
//
//                Flowable.
//                        create(new FlowableOnSubscribe<String>() {
//                            @Override
//                            public void subscribe(FlowableEmitter<String> e) throws Exception {
//                                if (!e.isCancelled()) {
//                                    e.onNext("1");
//                                    e.onNext("2");
//                                    e.onComplete();
//                                }
//                            }
//                        }, BackpressureStrategy.DROP).
//                        map(new Function<String, Integer>() {
//                            @Override
//                            public Integer apply(String s) throws Exception {
//                                return Integer.parseInt(s);
//                            }
//                        }).
//                        subscribe(new Subscriber<Integer>() {
//                            @Override
//                            public void onSubscribe(Subscription s) {
//                                s.request(Long.MAX_VALUE);
//                                System.out.println("onSubscribe");
//                            }
//
//                            @Override
//                            public void onNext(Integer integer) {
//                                System.out.println("onNext:" + integer);
//                            }
//
//                            @Override
//                            public void onError(Throwable t) {
//
//                            }
//
//                            @Override
//                            public void onComplete() {
//                                System.out.println("onComplete");
//                            }
//                        });
            }
        });
    }
}
