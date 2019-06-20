package com.sxdsf.sample.chapter4.lesson6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sxdsf.async.imitate2.Function;
import com.sxdsf.async.imitate2.backpressure.Drop;
import com.sxdsf.async.imitate2.backpressure.Receiver;
import com.sxdsf.async.imitate2.backpressure.Telephoner;
import com.sxdsf.async.imitate2.backpressure.TelephonerEmitter;
import com.sxdsf.async.imitate2.backpressure.TelephonerOnCall;
import com.sxdsf.async.imitate2.backpressure.TelephonerOperator;
import com.sxdsf.sample.R;

public class Lesson4_6Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson4_6);
        findViewById(R.id.async).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Telephoner.
                        create(new TelephonerOnCall<String>() {
                            @Override
                            public void call(TelephonerEmitter<String> telephonerEmitter) {
                                telephonerEmitter.onReceive("1");
                                telephonerEmitter.onReceive("2");
                                telephonerEmitter.onCompleted();
                            }
                        }).
                        map(new Function<String, Integer>() {
                            @Override
                            public Integer call(String s) {
                                return Integer.parseInt(s);
                            }
                        }).
                        call(new Receiver<Integer>() {
                            @Override
                            public void onCall(Drop d) {
                                d.request(Long.MAX_VALUE);
                                System.out.println("onCall");
                            }

                            @Override
                            public void onReceive(Integer integer) {
                                System.out.println("onReceive:" + integer);
                            }

                            @Override
                            public void onError(Throwable t) {
                                System.out.println("onError");
                            }

                            @Override
                            public void onCompleted() {
                                System.out.println("onCompleted");
                            }
                        });

                Telephoner.
                        create(new TelephonerOnCall<String>() {
                            @Override
                            public void call(TelephonerEmitter<String> telephonerEmitter) {
                                telephonerEmitter.onReceive("3");
                                telephonerEmitter.onReceive("4");
                                telephonerEmitter.onCompleted();
                            }
                        }).
                        lift(new TelephonerOperator<Integer, String>() {
                            @Override
                            public Receiver<String> call(final Receiver<Integer> receiver) {
                                return new Receiver<String>() {
                                    @Override
                                    public void onCall(Drop d) {
                                        receiver.onCall(d);
                                    }

                                    @Override
                                    public void onReceive(String s) {
                                        receiver.onReceive(Integer.parseInt(s));
                                    }

                                    @Override
                                    public void onError(Throwable t) {
                                        receiver.onError(t);
                                    }

                                    @Override
                                    public void onCompleted() {
                                        receiver.onCompleted();
                                    }
                                };
                            }
                        }).
                        call(new Receiver<Integer>() {
                            @Override
                            public void onCall(Drop d) {
                                d.request(Long.MAX_VALUE);
                                System.out.println("onCall");
                            }

                            @Override
                            public void onReceive(Integer integer) {
                                System.out.println("onReceive:" + integer);
                            }

                            @Override
                            public void onError(Throwable t) {
                                System.out.println("onError");
                            }

                            @Override
                            public void onCompleted() {
                                System.out.println("onCompleted");
                            }
                        });
            }
        });
    }
}
