package com.example.shapelibtest;

public class JniTest {
	static final String TAG = "JniTest";
	
	static {
		System.loadLibrary("ShapLibTest");
	}

	public static native void helloWorld(String aaa);

}
