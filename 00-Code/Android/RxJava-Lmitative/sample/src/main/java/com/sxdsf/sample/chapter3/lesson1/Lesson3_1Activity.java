package com.sxdsf.sample.chapter3.lesson1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sxdsf.sample.R;


public class Lesson3_1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson3_1);
        findViewById(R.id.btnRxJava1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Subscription tSubscription = Observable.create(new Observable.OnSubscribe<String>() {
//                    @Override
//                    public void call(Subscriber<? super String> subscriber) {
//                        if (!subscriber.isUnsubscribed()) {
//                            subscriber.onNext("test");
//                            subscriber.onCompleted();
//                        }
//                    }
//                }).subscribe(new Observer<String>() {
//                    @Override
//                    public void onCompleted() {
//                        System.out.println("onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        System.out.println("onNext:" + s);
//                    }
//                });
            }
        });

        findViewById(R.id.btnRxJava2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Observable.create(new ObservableOnSubscribe<String>() {
//                    @Override
//                    public void subscribe(ObservableEmitter<String> e) throws Exception {
//                        if (!e.isDisposed()) {
//                            e.onNext("1");
//                            e.onNext("2");
//                            e.onNext("3");
//                            e.onNext("4");
//                            e.onNext("5");
//                            e.onNext("6");
//                            e.onNext("7");
//                            e.onNext("8");
//                            e.onNext("9");
//                            e.onNext("10");
//                            e.onComplete();
//                        }
//                    }
//                }).subscribe(new Observer<String>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        System.out.println("onSubscribe");
//                    }
//
//                    @Override
//                    public void onNext(String value) {
//                        System.out.println("onNext:" + value);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        System.out.println("onCompleted");
//                    }
//                });
//
//                Flowable.create(new FlowableOnSubscribe<String>() {
//                    @Override
//                    public void subscribe(FlowableEmitter<String> e) throws Exception {
//                        if (!e.isCancelled()) {
//                            e.onNext("test");
//                            e.onComplete();
//                        }
//                    }
//                }, BackpressureStrategy.DROP).subscribe(new Subscriber<String>() {
//                    @Override
//                    public void onSubscribe(Subscription s) {
//                        s.request(Long.MAX_VALUE);
//                        System.out.println("onSubscribe");
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        System.out.println("onNext:" + s);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        System.out.println("onCompleted");
//                    }
//                });
            }
        });
    }
}
