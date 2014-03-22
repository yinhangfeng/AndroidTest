package com.example.androidtest;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class BitmapActivity extends Activity implements OnClickListener {

	private ImageView imageView;
	private Bitmap big;
	private Bitmap small;
	
	private long startTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bitmap);
		
		findViewById(R.id.bitmap_size).setOnClickListener(this);
		findViewById(R.id.bitmap_loop_set_small).setOnClickListener(this);
		findViewById(R.id.bitmap_loop_set_big).setOnClickListener(this);
		findViewById(R.id.bitmap_recycle).setOnClickListener(this);
		imageView = (ImageView) findViewById(R.id.image_view);
		
		big = U.getBitmapFromResources(this, R.drawable.testl1);
		small = U.getBitmapFromResources(this, R.drawable.tests1);
		imageView.setImageBitmap(big);
	}
	
	private void loopSet(boolean b) {
		final Bitmap bm = b ? big : small;
		startTime = System.nanoTime();
		Handler h = new Handler();
		Runnable task = new Runnable() {
			@Override
			public void run() {
				long time = System.nanoTime();
				L.i("set bm time=" + (time - startTime) + "ms=" + ((time - startTime) / 1000 / 1000));
				imageView.setImageBitmap(bm);
			}
		};
		for(int i = 0; i < 100; ++i) {
			h.post(task);
		}
	}
	
	private void drawBitmapTest(boolean b) {
		final Bitmap bm = b ? big : small;
		Canvas canvas = new Canvas();
		Paint paint = new Paint();
		long start = System.nanoTime();
		for(int i = 0; i < 10000; ++i) {
			canvas.drawBitmap(bm, 0, 0, paint);
			canvas.drawLine(0, 0, i, i, paint);
		}
		long end = System.nanoTime();
		L.i("drawBitmapTest time=", end - start, ((end - start) / 1000 / 1000));
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.bitmap_size:
			int smallSize = U.getBitmapSize(small);
			int bigSize = U.getBitmapSize(big);
			L.i("small size ", smallSize, smallSize / 1024);
			L.i("big size ", bigSize, bigSize / 1024);
			break;
		case R.id.bitmap_loop_set_small:
			drawBitmapTest(false);
			//loopSet(false);
			break;
		case R.id.bitmap_loop_set_big:
			drawBitmapTest(true);
			//loopSet(true);
			break;
		case R.id.bitmap_recycle:
			small.recycle();
			big.recycle();
			imageView.invalidate();
			break;
		}
		
	}

}
