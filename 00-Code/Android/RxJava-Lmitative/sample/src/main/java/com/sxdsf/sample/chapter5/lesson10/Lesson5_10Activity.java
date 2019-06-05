package com.sxdsf.sample.chapter5.lesson10;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sxdsf.async.imitate2.Callee;
import com.sxdsf.async.imitate2.Caller;
import com.sxdsf.async.imitate2.CallerEmitter;
import com.sxdsf.async.imitate2.CallerOnCall;
import com.sxdsf.async.imitate2.NewThreadSwitcher;
import com.sxdsf.async.imitate2.Release;
import com.sxdsf.sample.R;

public class Lesson5_10Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson5_10);
        findViewById(R.id.async).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Caller.
                        create(new CallerOnCall<String>() {
                            @Override
                            public void call(CallerEmitter<String> callerEmitter) {
                                callerEmitter.onReceive("test");
                                callerEmitter.onCompleted();
                            }
                        }).
                        callOn(new NewThreadSwitcher()).
                        call(new Callee<String>() {
                            @Override
                            public void onCall(Release release) {

                            }

                            @Override
                            public void onReceive(String s) {
                                System.out.println("currentThread:" + Thread.currentThread());
                                System.out.println("onReceive" + s);
                            }

                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable t) {

                            }
                        });
            }
        });
    }
}
