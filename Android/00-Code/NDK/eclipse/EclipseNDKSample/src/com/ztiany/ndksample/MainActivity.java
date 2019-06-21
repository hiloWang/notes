package com.ztiany.ndksample;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	static{
		System.loadLibrary("Hello");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void callC(View view){
		String callC = callC();
		Toast.makeText(this, callC, Toast.LENGTH_LONG).show();
	}
	
	public native String callC();
	
}
