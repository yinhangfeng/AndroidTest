package com.example.shapelibtest;

import java.io.File;

import com.example.commonlibrary.DirectoryUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {
	static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		JniTest.helloWorld("xxx");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testRead(View v) {
		Log.i(TAG, "testRead");
		new AsyncTask() {

			@Override
			protected Object doInBackground(Object... params) {
				try {
					ShapLibTest.testRead("/sdcard/ShapeLibTest/shap_84/WF84DK");
				} catch(Exception e) {
					Log.e(TAG, Log.getStackTraceString(e));
				}
				Log.i(TAG, "testRead end");
				return null;
			}
		}.execute();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void test(View v) {
		Log.i(TAG, "test");
		new AsyncTask() {

			@Override
			protected Object doInBackground(Object... params) {
				File[] files = DirectoryUtils.getMountedVolumeFiles(MainActivity.this);
				File sdFile = new File(files[0], "test.xml");
				File esdFile = new File(files[1], "test.xml");
				Log.i(TAG, "sdFile=" + sdFile.getAbsolutePath() + " sdFile.exists()=" + sdFile.exists() + " esdFile=" + esdFile.getAbsolutePath() + " esdFile.exists()=" + esdFile.exists());
				ShapLibTest.test(sdFile.getAbsolutePath(), esdFile.getAbsolutePath());
				Log.i(TAG, "test end");
				return null;
			}
		}.execute();
	}

}
