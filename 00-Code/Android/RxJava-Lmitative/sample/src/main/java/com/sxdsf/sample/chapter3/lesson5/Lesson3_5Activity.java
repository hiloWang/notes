package com.sxdsf.sample.chapter3.lesson5;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sxdsf.async.imitate1.Caller;
import com.sxdsf.async.imitate1.Calling;
import com.sxdsf.async.imitate1.Receiver;
import com.sxdsf.sample.R;

public class Lesson3_5Activity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson3_5);
        findViewById(R.id.async).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calling tCalling = Caller.create(new Caller.OnCall<String>() {
                    @Override
                    public void call(Receiver<String> stringReceiver) {
                        if (!stringReceiver.isUnCalled()) {
                            stringReceiver.onReceive("test");
                            stringReceiver.onCompleted();
                        }
                    }
                }).call(new Receiver<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onReceive(String s) {
                        System.out.println("onReceive:" + s);
                    }
                });
            }
        });
    }
}
