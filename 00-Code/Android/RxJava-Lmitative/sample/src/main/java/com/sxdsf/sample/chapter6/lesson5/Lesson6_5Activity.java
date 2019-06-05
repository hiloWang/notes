package com.sxdsf.sample.chapter6.lesson5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sxdsf.async.imitate2.Callee;
import com.sxdsf.async.imitate2.Caller;
import com.sxdsf.async.imitate2.CallerConverter;
import com.sxdsf.async.imitate2.CallerEmitter;
import com.sxdsf.async.imitate2.CallerOnCall;
import com.sxdsf.async.imitate2.LooperSwitcher;
import com.sxdsf.async.imitate2.NewThreadSwitcher;
import com.sxdsf.async.imitate2.Release;
import com.sxdsf.async.imitate2.backpressure.Drop;
import com.sxdsf.async.imitate2.backpressure.Receiver;
import com.sxdsf.async.imitate2.backpressure.Telephoner;
import com.sxdsf.async.imitate2.backpressure.TelephonerConverter;
import com.sxdsf.async.imitate2.backpressure.TelephonerEmitter;
import com.sxdsf.async.imitate2.backpressure.TelephonerOnCall;
import com.sxdsf.sample.R;

public class Lesson6_5Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson6_5);
        findViewById(R.id.async).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findViewById(R.id.async).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Caller.
                                create(new CallerOnCall<String>() {
                                    @Override
                                    public void call(CallerEmitter<String> callerEmitter) {
                                        callerEmitter.onReceive("test");
                                        System.out.println("currentThread:" + Thread.currentThread());
                                    }
                                }).
                                unify(new CallerConverter<String, String>() {
                                    @Override
                                    public Caller<String> convert(Caller<String> caller) {
                                        return caller.
                                                callOn(new NewThreadSwitcher()).
                                                callbackOn(new LooperSwitcher(getMainLooper()));
                                    }
                                }).
                                call(new Callee<String>() {
                                    @Override
                                    public void onCall(Release release) {

                                    }

                                    @Override
                                    public void onReceive(String s) {
                                        System.out.println("onReceive:" + s);
                                        System.out.println("currentThread:" + Thread.currentThread());
                                    }

                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable t) {

                                    }
                                });

                        Telephoner.
                                create(new TelephonerOnCall<String>() {
                                    @Override
                                    public void call(TelephonerEmitter<String> telephonerEmitter) {
                                        telephonerEmitter.onReceive("test");
                                        System.out.println("currentThread:" + Thread.currentThread());
                                    }
                                }).
                                unify(new TelephonerConverter<String, String>() {
                                    @Override
                                    public Telephoner<String> convert(Telephoner<String> telephoner) {
                                        return telephoner.
                                                callOn(new NewThreadSwitcher()).
                                                callbackOn(new LooperSwitcher(getMainLooper()));
                                    }
                                }).
                                call(new Receiver<String>() {
                                    @Override
                                    public void onCall(Drop d) {
                                        d.request(Long.MAX_VALUE);
                                    }

                                    @Override
                                    public void onReceive(String s) {
                                        System.out.println("onReceive:" + s);
                                        System.out.println("currentThread:" + Thread.currentThread());
                                    }

                                    @Override
                                    public void onError(Throwable t) {

                                    }

                                    @Override
                                    public void onCompleted() {

                                    }
                                });
                    }
                });
            }
        });
    }
}
