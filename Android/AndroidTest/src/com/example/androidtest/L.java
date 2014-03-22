package com.example.androidtest;

import java.util.Arrays;

import android.util.Log;

public final class L {
	private static final String TAG = "AndroidTest";
	private static volatile boolean DISABLED = false;

	private L() {
	}

	public static void enableLogging() {
		DISABLED = false;
	}

	public static void disableLogging() {
		DISABLED = true;
	}

	public static void d(String message, Object... args) {
		log(Log.DEBUG, null, message, args);
	}

	public static void i(String message, Object... args) {
		log(Log.INFO, null, message, args);
	}

	public static void w(String message, Object... args) {
		log(Log.WARN, null, message, args);
	}

	public static void e(Throwable ex) {
		log(Log.ERROR, ex, null);
	}

	public static void e(String message, Object... args) {
		log(Log.ERROR, null, message, args);
	}

	public static void e(Throwable ex, String message, Object... args) {
		log(Log.ERROR, ex, message, args);
	}

	private static void log(int priority, Throwable ex, String message, Object... args) {
		if (DISABLED) return;
		if (args != null && args.length > 0) {
			message += " " + Arrays.toString(args);
		}
		String log;
		if (ex == null) {
			log = message;
		} else {
			String logMessage = message == null ? ex.getMessage() : message;
			String logBody = Log.getStackTraceString(ex);
			log = logMessage + "\n" + logBody;
		}
		Log.println(priority, TAG, log);
	}
}