package com.ztiany.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-08-05 15:27
 */
public class ContentActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    public static Intent getLaunchIntent(Context context, String title, Class fragment) {
        Intent intent = new Intent(context, ContentActivity.class);
        intent.putExtra("key1", title);
        intent.putExtra("key2", fragment.getName());
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_content);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setContentInsetStartWithNavigation(0);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    supportFinishAfterTransition();
                }
            });
        }

        if (savedInstanceState == null) {
            String name = getIntent().getStringExtra("key2");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_content, Fragment.instantiate(this, name, null), name)
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String title = getIntent().getStringExtra("key1");
        Log.d("ContentActivity", title);
        mToolbar.setTitle(title);
    }
}
