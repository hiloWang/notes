package com.sxdsf.sample.chapter4.lesson5;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sxdsf.async.imitate2.Callee;
import com.sxdsf.async.imitate2.Caller;
import com.sxdsf.async.imitate2.CallerEmitter;
import com.sxdsf.async.imitate2.CallerOnCall;
import com.sxdsf.async.imitate2.CallerOperator;
import com.sxdsf.async.imitate2.Function;
import com.sxdsf.async.imitate2.Release;
import com.sxdsf.sample.R;

public class Lesson4_5Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson4_5);
        findViewById(R.id.async).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Caller.
                        create(new CallerOnCall<String>() {
                            @Override
                            public void call(CallerEmitter<String> callerEmitter) {
                                callerEmitter.onReceive("1");
                                callerEmitter.onReceive("2");
                                callerEmitter.onCompleted();
                            }
                        }).
                        map(new Function<String, Integer>() {
                            @Override
                            public Integer call(String s) {
                                return Integer.parseInt(s);
                            }
                        }).
                        call(new Callee<Integer>() {
                            @Override
                            public void onCall(Release release) {
                                System.out.println("onCall");
                            }

                            @Override
                            public void onReceive(Integer integer) {
                                System.out.println("onReceive:" + integer);
                            }

                            @Override
                            public void onCompleted() {
                                System.out.println("onCompleted");
                            }

                            @Override
                            public void onError(Throwable t) {

                            }
                        });

                Caller.
                        create(new CallerOnCall<String>() {
                            @Override
                            public void call(CallerEmitter<String> callerEmitter) {
                                callerEmitter.onReceive("3");
                                callerEmitter.onReceive("4");
                                callerEmitter.onCompleted();
                            }
                        }).
                        lift(new CallerOperator<Integer, String>() {
                            @Override
                            public Callee<String> call(final Callee<Integer> callee) {
                                return new Callee<String>() {
                                    @Override
                                    public void onCall(Release release) {
                                        callee.onCall(release);
                                    }

                                    @Override
                                    public void onReceive(String s) {
                                        callee.onReceive(Integer.parseInt(s));
                                    }

                                    @Override
                                    public void onCompleted() {
                                        callee.onCompleted();
                                    }

                                    @Override
                                    public void onError(Throwable t) {
                                        callee.onError(t);
                                    }
                                };
                            }
                        }).
                        call(new Callee<Integer>() {
                            @Override
                            public void onCall(Release release) {
                                System.out.println("onCall");
                            }

                            @Override
                            public void onReceive(Integer integer) {
                                System.out.println("onReceive:" + integer);
                            }

                            @Override
                            public void onCompleted() {
                                System.out.println("onCompleted");
                            }

                            @Override
                            public void onError(Throwable t) {
                                System.out.println("onError");
                            }
                        });
            }
        });
    }
}
