package com.example.androidconcepts;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView tv1;
	//IncomingHandler resultHandler;
	Handler mHandler;
	MyThread t1;
	
	SharedPreferences mPrefs;
	Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(mPrefs.getString("author","").equals("")) {
			editor = mPrefs.edit();
			editor.putString("author", "Sourav Basu");
			editor.commit();
		}
		
		tv1 = (TextView) findViewById(R.id.tv1);
		t1 = new MyThread();
		
		// attaching Handler to the UI Thread - Older way		
		//resultHandler = new IncomingHandler(); 
		
		// Defines a Handler object that's attached to the UI thread
		mHandler = new Handler(Looper.getMainLooper()) {
	    	@Override
	        public void handleMessage(Message msg)
	        {
	        	String res = msg.getData().getString("result");
	        	tv1.setText(res);
	        }
	    };

		
		Button btnStart = (Button) findViewById(R.id.btnStart);
		btnStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				t1.start();
			}
		});
		
		Button btnStop = (Button) findViewById(R.id.btnStop);
		btnStop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), HandlerExampleActivity.class);
				//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
			}
		});
		
	}
	
	class IncomingHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
        	String res = msg.getData().getString("result");
        	tv1.setText(res);
        }
    }

	public class MyThread extends Thread {

		@Override
		public void run() {
			while(true) {
				String text = "Random number is: "+Math.random();
				
				Bundle msgBundle = new Bundle();
			    msgBundle.putString("result", text);
			    
			    Message msg = new Message();
			    msg.setData(msgBundle);
			    
			    mHandler.sendMessage(msg);
			    try {
					MyThread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}