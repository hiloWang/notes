package com.ztiany.androidipc.activitys;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ztiany.androidipc.ICompute;
import com.ztiany.androidipc.ISecurityCenter;
import com.ztiany.androidipc.R;
import com.ztiany.androidipc.service.binderpool.BinderPool;
import com.ztiany.androidipc.service.binderpool.Compute;
import com.ztiany.androidipc.service.binderpool.SecurityCenter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BinderPollActivity extends AppCompatActivity {

    private EditText mAEt;
    private EditText mBEt;
    private EditText mTextEt;
    private RadioGroup mRadioGroup;
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(3);

    private BinderPool mBinderPool;
    private ISecurityCenter mISecurityCenter;
    private ICompute mICompute;
    private boolean mIsEn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_poll);
        findViews();
        initEvent();
        init();
    }

    private void initEvent() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.id_act_binder_pool_en_rbtn) {
                    mIsEn = true;
                } else if (checkedId == R.id.id_act_binder_pool_de_rbtn) {
                    mIsEn = false;
                }
            }
        });
    }

    private void init() {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                mBinderPool = BinderPool.getInstance(BinderPollActivity.this);
                mICompute = Compute.asInterface(mBinderPool.queryBinder(BinderPool.BINDER_COMPUTE));
                mISecurityCenter = SecurityCenter.asInterface(mBinderPool.queryBinder(BinderPool.BINDER_SECURITY));
            }
        });
    }

    private void findViews() {
        mAEt = (EditText) findViewById(R.id.id_act_binder_a_et);
        mBEt = (EditText) findViewById(R.id.id_act_binder_b_et);
        mTextEt = (EditText) findViewById(R.id.id_act_binder_pool_string_et);
        mRadioGroup = (RadioGroup) findViewById(R.id.id_act_binder_pool_rg);
    }

    public void add(View view) {
        if (mICompute == null) {
            Toast.makeText(BinderPollActivity.this, "not init", Toast.LENGTH_SHORT).show();
            return;
        }

        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    int a = Integer.parseInt(mAEt.getText().toString());
                    int b = Integer.parseInt(mBEt.getText().toString());
                    Log.d("BinderPollActivity", "mICompute.add(a, b):" + mICompute.add(a, b));
                } catch (RemoteException e) {
                    e.printStackTrace();
                    Log.e("BinderPollActivity", e.getLocalizedMessage());
                }
            }
        });
    }

    public void deOrEn(View view) {
        if (mISecurityCenter == null) {
            Toast.makeText(BinderPollActivity.this, "not init", Toast.LENGTH_SHORT).show();
            return;
        }

      mExecutorService.execute(new Runnable() {
          @Override
          public void run() {
              String text = mTextEt.getText().toString();
              try {
                  if(mIsEn)
                  Log.d("BinderPollActivity", "en = "+mISecurityCenter.encrypt(text));
                  else
                      Log.d("BinderPollActivity", "de = "+mISecurityCenter.decrypt(text));

              } catch (RemoteException e) {
                  e.printStackTrace();
              }
          }
      });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("BinderPollActivity", "onStop");
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d("BinderPollActivity", "onDetachedFromWindow");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
