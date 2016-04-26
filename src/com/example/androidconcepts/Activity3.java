package com.example.androidconcepts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Activity3 extends Activity {
	
	int initial_val = 0;
	Handler handler = null;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity3);
		
		final EditText et1 = (EditText) findViewById(R.id.et1);
		
		final MyThread t1 = new MyThread(Integer.parseInt(et1.getText().toString()));
		
		Button btnStart = (Button) findViewById(R.id.btnStart);
		btnStart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (t1.getState() == Thread.State.NEW)
					t1.start();
				else {
					t1.resumeThread();
				}
			}
		});
		
		Button btnPause = (Button) findViewById(R.id.btnPause);
		btnPause.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				t1.pauseThread();
			}
		});
		
		Button btnA4 = (Button) findViewById(R.id.btnA4);
		btnA4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(Activity3.this, Activity4.class);
				startActivity(i);
			}
		});
		
		//register a handler with the UI thread which listens to incoming messages from other threads
		handler = new Handler(Looper.getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {
				et1.setText(msg.getData().getString("result"));
			}
		};
		
		
	}
	
	class MyThread extends Thread {
		int val = 0;
		private boolean paused = false;
		
		public MyThread(int val) {
			this.val = val;
		}
		
		
		@Override
		public void run() {
			try {
				while(true) {
					val += 5;

					Bundle bundle = new Bundle();
					bundle.putString("result", val + "");

					Message msg = new Message();
					msg.setData(bundle);

					handler.sendMessage(msg);
					
					Thread.sleep(1000);
					synchronized (this) {
						while (paused)
							wait();
					}
				}
			} catch (InterruptedException exc) {
				System.out.println(getName() + " interrupted.");
			}
			//System.out.println("\n" + getName() + " exiting.");
		}

		public synchronized void pauseThread() {
			paused = true;
		}

		public synchronized void resumeThread() {
			paused = false;
			notify();
		}
	}

}
