package com.example.androidconcepts;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.view.View;

public class Activity5 extends Activity {
	
	ProgressDialog dialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity5);
		
		Button btnDownloadPDF = (Button) findViewById(R.id.btnDownloadPDF);
		btnDownloadPDF.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog = ProgressDialog.show(Activity5.this, "", "Loading...pls wait");
				Thread t = new Thread(new MyThread());
				t.start();
			}
		});
		
		Button btnA6 = (Button) findViewById(R.id.btnA6);
		btnA6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i  = new Intent(Activity5.this, Activity6.class);
				startActivity(i);
			}
		});
	}
	
	private Handler messageHandler = new Handler() {
        @Override
		public void handleMessage(Message msg) {
        	super.handleMessage(msg);
        	//handler designed in such a way that whenever it receives a message from a thread it will call dismiss method
            dialog.dismiss();
            Toast.makeText(Activity5.this, "PDF successfully downloaded", Toast.LENGTH_SHORT).show();
        }
    };
	
	class MyThread implements Runnable {

		@Override
		public void run() {
			File pdf = downloadPDF();
			messageHandler.sendEmptyMessage(0);
		}
		
		public File downloadPDF() {
			File downloaded_file = null;
			
			File root = Environment.getExternalStorageDirectory();
			File dir = new File(root.getAbsolutePath() + "/MyTestApp");
			if(!dir.exists())
				dir.mkdir();
			
			try {
				URL url = new URL("http://www.redbooks.ibm.com/redbooks/pdfs/sg247994.pdf");
				downloaded_file = new File(dir, "test.pdf");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				InputStream is = con.getInputStream();
				
				byte[] data = new byte[1024000];
				int bytesRead = is.read(data);			
				while(bytesRead!=-1) {
					//System.out.println(bytesRead);
					bytesRead = is.read(data); //reading data from the inputsream and storing them in a buffer array, until bytesRead is -1
				}
				
				FileOutputStream fos = new FileOutputStream(downloaded_file);
		        fos.write(data);
		        fos.flush();
		        fos.close();
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return downloaded_file;
		}
	}
	
	
}
