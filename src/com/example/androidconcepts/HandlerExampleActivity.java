package com.example.androidconcepts;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class HandlerExampleActivity extends Activity {
    private ProgressDialog progressDialog;
    private ImageView imageView;
    private String url = "http://images.scholastic.co.uk/assets/a/5d/28/486840.jpg";
    private String image_url = "https://s3-us-west-2.amazonaws.com/motomaker/images/renders/mm-com/FLEXR6/MMLanding_Large_Carousel_MotoX2_1.png";
    private Bitmap bitmap = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handler_example_activity);
        imageView = (ImageView) findViewById(R.id.im1);

        Button start = (Button) findViewById(R.id.btnDownload);
        start.setOnClickListener(new OnClickListener() {
        	@Override
            public void onClick(View arg0) {
        		progressDialog = ProgressDialog.show(HandlerExampleActivity.this, "", "Loading..");
        		new Thread() {
        			public void run() {
        				bitmap = downloadImage(image_url);
        				messageHandler.sendEmptyMessage(0);
        			}
        		}.start();
            }
        });
        
        Button btnA3 = (Button) findViewById(R.id.btnA3);
        btnA3.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View arg) {
        		Intent i = new Intent(HandlerExampleActivity.this, Activity3.class);
        		startActivity(i);
        	}
        });
    }

    private Handler messageHandler = new Handler() {
    	@Override
        public void handleMessage(Message msg) {
        	super.handleMessage(msg);
        	imageView.setImageBitmap(bitmap);
            progressDialog.dismiss();
        }
    };
    
    private Bitmap downloadImage(String url) {
    	Bitmap bitmap_image = null;
    	URL url_obj;
    	InputStream is = null;
		try {
			url_obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) url_obj.openConnection();
			is = con.getInputStream();
			
			bitmap_image = BitmapFactory.decodeStream(is);
			
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(is!=null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return bitmap_image;
    }

    private Bitmap downloadBitmap(String url) {
        // Initialize the default HTTP client object
        final DefaultHttpClient client = new DefaultHttpClient();

        //forming a HttpGet request
        final HttpGet getRequest = new HttpGet(url);
        try {
            HttpResponse response = client.execute(getRequest);
            //check 200 OK for success
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url);
                return null;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    // getting contents from the stream
                    inputStream = entity.getContent();
                    // decoding stream data back into image Bitmap
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            getRequest.abort();
            Log.e(getString(R.string.app_name), "Error "+ e.toString());
        }
        return null;
    }
}