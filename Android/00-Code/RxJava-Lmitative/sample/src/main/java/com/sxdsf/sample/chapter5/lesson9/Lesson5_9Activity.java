package com.sxdsf.sample.chapter5.lesson9;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sxdsf.async.imitate1.Caller;
import com.sxdsf.async.imitate1.NewThreadSwitcher;
import com.sxdsf.async.imitate1.Receiver;
import com.sxdsf.sample.R;

public class Lesson5_9Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson5_9);
        findViewById(R.id.async).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Caller.
                        create(new Caller.OnCall<String>() {
                            @Override
                            public void call(Receiver<String> stringReceiver) {
                                if (!stringReceiver.isUnCalled()) {
                                    stringReceiver.onReceive("test");
                                    stringReceiver.onCompleted();
                                }
                            }
                        }).
                        callOn(new NewThreadSwitcher()).
                        call(new Receiver<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onReceive(String s) {
                                System.out.println("currentThread:" + Thread.currentThread());
                                System.out.println("onReceive:" + s);
                            }
                        });
            }
        });
    }
}
