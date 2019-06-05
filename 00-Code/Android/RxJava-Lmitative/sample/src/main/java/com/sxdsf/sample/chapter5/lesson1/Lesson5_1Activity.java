package com.sxdsf.sample.chapter5.lesson1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sxdsf.sample.R;


public class Lesson5_1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson5_1);
        findViewById(R.id.btnRxJava1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Observable.
//                        create(new Observable.OnSubscribe<String>() {
//                            @Override
//                            public void call(Subscriber<? super String> subscriber) {
//                                if (!subscriber.isUnsubscribed()) {
//                                    System.out.println("currentThread:" + Thread.currentThread());
//                                    subscriber.onNext("test");
//                                    subscriber.onCompleted();
//                                }
//                            }
//                        }).
//                        subscribeOn(Schedulers.newThread()).
//                        observeOn(AndroidSchedulers.mainThread()).
//                        subscribe(new Observer<String>() {
//                            @Override
//                            public void onCompleted() {
//
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//
//                            }
//
//                            @Override
//                            public void onNext(String s) {
//                                System.out.println("onNext:" + s + "currentThread:" + Thread.currentThread());
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
//                                    System.out.println("currentThread:" + Thread.currentThread());
//                                    e.onNext("test");
//                                    e.onComplete();
//                                }
//                            }
//                        }).
//                        subscribeOn(Schedulers.newThread()).
//                        observeOn(AndroidSchedulers.mainThread()).
//                        subscribe(new Observer<String>() {
//                            @Override
//                            public void onSubscribe(Disposable d) {
//
//                            }
//
//                            @Override
//                            public void onNext(String value) {
//                                System.out.println("onNext:" + value + "currentThread:" + Thread.currentThread());
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//
//                            }
//
//                            @Override
//                            public void onComplete() {
//
//                            }
//                        });
//
//                Flowable.
//                        create(new FlowableOnSubscribe<String>() {
//                            @Override
//                            public void subscribe(FlowableEmitter<String> e) throws Exception {
//                                if (!e.isCancelled()) {
//                                    System.out.println("currentThread:" + Thread.currentThread());
//                                    e.onNext("test");
//                                    e.onComplete();
//                                }
//                            }
//                        }, BackpressureStrategy.DROP).
//                        subscribeOn(Schedulers.newThread()).
//                        observeOn(AndroidSchedulers.mainThread()).
//                        subscribe(new Subscriber<String>() {
//                            @Override
//                            public void onSubscribe(Subscription s) {
//                                s.request(Long.MAX_VALUE);
//                            }
//
//                            @Override
//                            public void onNext(String s) {
//                                System.out.println("onNext:" + s + "currentThread:" + Thread.currentThread());
//                            }
//
//                            @Override
//                            public void onError(Throwable t) {
//
//                            }
//
//                            @Override
//                            public void onComplete() {
//
//                            }
//                        });
            }
        });
    }
}
