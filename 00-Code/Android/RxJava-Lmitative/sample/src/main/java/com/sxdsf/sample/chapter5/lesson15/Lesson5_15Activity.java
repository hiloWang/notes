package com.sxdsf.sample.chapter5.lesson15;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sxdsf.async.imitate1.Caller;
import com.sxdsf.async.imitate1.LooperSwitcher;
import com.sxdsf.async.imitate1.NewThreadSwitcher;
import com.sxdsf.async.imitate1.Receiver;
import com.sxdsf.sample.R;

public class Lesson5_15Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson5_15);
        findViewById(R.id.async).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Caller.
                        create(new Caller.OnCall<String>() {
                            @Override
                            public void call(Receiver<String> stringReceiver) {
                                stringReceiver.onReceive("test");
                                System.out.println("currentThread:" + Thread.currentThread());
                                stringReceiver.onCompleted();
                            }
                        }).
                        callOn(new NewThreadSwitcher()).
                        callbackOn(new LooperSwitcher(getMainLooper())).
                        call(new Receiver<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onReceive(String s) {
                                System.out.println("onReceive:" + s);
                                System.out.println("currentThread:" + Thread.currentThread());
                            }
                        });
            }
        });
    }
}
