package com.example.androidtest;

import java.io.InputStream;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		findViewById(R.id.bitmap).setOnClickListener(this);
		findViewById(R.id.image_downloader).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.bitmap:
			startActivity(new Intent(this, BitmapActivity.class));
			break;
		case R.id.image_downloader:
			final ImageDownloader l = new ImageDownloader();
			new AsyncTask<Void, Void, Void>() {

				@Override
				protected Void doInBackground(Void... params) {
					try {
						InputStream is = l.getStream("http://img1.gamersky.com/image2014/03/20140322tqy_1/gamersky_02origin_03_20143221737D7D.jpg");
						long start = System.nanoTime();
						L.i("is.available=", is.available());
						long end = System.nanoTime();
						U.logTimeIntervalNano(start, end);
						
					} catch(Exception e) {
						L.e(e);
					}
					return null;
				}
				
			}.execute();
			break;
		}
		
	}

}
