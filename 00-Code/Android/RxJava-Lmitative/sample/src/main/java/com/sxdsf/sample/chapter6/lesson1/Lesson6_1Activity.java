package com.sxdsf.sample.chapter6.lesson1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sxdsf.sample.R;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class Lesson6_1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson6_1);
        findViewById(R.id.btnRxJava1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Observable.
//                        create(new Observable.OnSubscribe<String>() {
//                            @Override
//                            public void call(Subscriber<? super String> subscriber) {
//                                if (!subscriber.isUnsubscribed()) {
//                                    subscriber.onNext("test");
//                                    System.out.println("currentThread:" + Thread.currentThread());
//                                    subscriber.onCompleted();
//                                }
//                            }
//                        }).
//                        compose(new Observable.Transformer<String, String>() {
//                            @Override
//                            public Observable<String> call(Observable<String> stringObservable) {
//                                return stringObservable.
//                                        subscribeOn(Schedulers.newThread()).
//                                        observeOn(AndroidSchedulers.mainThread());
//                            }
//                        }).
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
//                                System.out.println("currentThread:" + Thread.currentThread());
//                                System.out.println("onNext:" + s);
//                            }
//                        });
            }
        });

        findViewById(R.id.btnRxJava2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Observable.
                        create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> e) throws Exception {
                                if (!e.isDisposed()) {
                                    e.onNext("test");
                                    System.out.println("currentThread:" + Thread.currentThread());
                                    e.onComplete();
                                }
                            }
                        }).
                        compose(new ObservableTransformer<String, String>() {
                            @Override
                            public ObservableSource<String> apply(Observable<String> upstream) {
                                return upstream.
                                        subscribeOn(Schedulers.newThread()).
                                        observeOn(AndroidSchedulers.mainThread());
                            }
                        }).
                        subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(String value) {
                                System.out.println("currentThread:" + Thread.currentThread());
                                System.out.println("onNext:" + value);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });

                Flowable.
                        create(new FlowableOnSubscribe<String>() {
                            @Override
                            public void subscribe(FlowableEmitter<String> e) throws Exception {
                                if (!e.isCancelled()) {
                                    e.onNext("test");
                                    System.out.println("currentThread:" + Thread.currentThread());
                                    e.onComplete();
                                }
                            }
                        }, BackpressureStrategy.DROP).
                        compose(new FlowableTransformer<String, String>() {
                            @Override
                            public Publisher<String> apply(Flowable<String> upstream) {
                                return upstream.
                                        subscribeOn(Schedulers.newThread()).
                                        observeOn(AndroidSchedulers.mainThread());
                            }
                        }).
                        subscribe(new Subscriber<String>() {
                            @Override
                            public void onSubscribe(Subscription s) {
                                s.request(Long.MAX_VALUE);
                            }

                            @Override
                            public void onNext(String s) {
                                System.out.println("currentThread:" + Thread.currentThread());
                                System.out.println("onNext:" + s);
                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
    }
}
