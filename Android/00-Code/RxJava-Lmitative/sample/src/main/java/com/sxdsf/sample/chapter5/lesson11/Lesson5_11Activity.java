package com.sxdsf.sample.chapter5.lesson11;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sxdsf.async.imitate2.NewThreadSwitcher;
import com.sxdsf.async.imitate2.backpressure.Drop;
import com.sxdsf.async.imitate2.backpressure.Receiver;
import com.sxdsf.async.imitate2.backpressure.Telephoner;
import com.sxdsf.async.imitate2.backpressure.TelephonerEmitter;
import com.sxdsf.async.imitate2.backpressure.TelephonerOnCall;
import com.sxdsf.sample.R;

public class Lesson5_11Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson5_11);
        findViewById(R.id.async).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Telephoner.
                        create(new TelephonerOnCall<String>() {
                            @Override
                            public void call(TelephonerEmitter<String> telephonerEmitter) {
                                telephonerEmitter.onReceive("test");
                                telephonerEmitter.onCompleted();
                            }
                        }).
                        callOn(new NewThreadSwitcher()).
                        call(new Receiver<String>() {
                            @Override
                            public void onCall(Drop d) {
                                d.request(Long.MAX_VALUE);
                            }

                            @Override
                            public void onReceive(String s) {
                                System.out.println("currentThread:" + Thread.currentThread());
                                System.out.println("onReceive:" + s);
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
}
