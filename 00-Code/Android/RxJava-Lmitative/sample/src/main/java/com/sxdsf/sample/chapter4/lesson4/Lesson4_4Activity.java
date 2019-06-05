package com.sxdsf.sample.chapter4.lesson4;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sxdsf.async.imitate1.Caller;
import com.sxdsf.async.imitate1.Func1;
import com.sxdsf.async.imitate1.Receiver;
import com.sxdsf.sample.R;

public class Lesson4_4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson4_4);
        findViewById(R.id.async).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Caller.
                        create(new Caller.OnCall<String>() {
                            @Override
                            public void call(Receiver<String> stringReceiver) {
                                if (!stringReceiver.isUnCalled()) {
                                    stringReceiver.onReceive("1");
                                    stringReceiver.onReceive("2");
                                    stringReceiver.onCompleted();
                                }
                            }
                        }).
                        map(new Func1<String, Integer>() {
                            @Override
                            public Integer call(String s) {
                                return Integer.parseInt(s);
                            }
                        }).
                        call(new Receiver<Integer>() {
                            @Override
                            public void onCompleted() {
                                System.out.println("onCompleted");
                            }

                            @Override
                            public void onError(Throwable t) {

                            }

                            @Override
                            public void onReceive(Integer integer) {
                                System.out.println("onReceive:" + integer);
                            }
                        });
            }
        });
    }
}
