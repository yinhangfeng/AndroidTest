package com.example.test;

import java.lang.reflect.Field;

public class CASTest {
	private static long aOffset;
	private volatile int a = 0;
	private static final int THREADS	= 32;
	private Thread[] threads = new Thread[THREADS];
	
	public static void test() {
		U.println("start test");
		CASTest test = new CASTest();
		test.printA();
		test.startThread();
		U.println("join...");
		test.join();
		test.printA();
	}
	
	public void printA() {
		U.println("a=" + a);
	}
	
	public void startThread() {
		for(int i = 0; i < 32; i++) {
			threads[i] = new MyThread();
			threads[i].start();
		}
	}
	
	public void join() {
		try {
			for(int i = 0; i < threads.length; i++) {
				threads[i].join();
			}
		} catch(InterruptedException e) {
			U.println("join error=" + e);
		}
	}
	
	private void incA() {
		for(;;) {
			int current = a;
			int next = current + 1;
			if(UNSAFE.compareAndSwapInt(this, aOffset, current, next)) {
				break;
			}
		}
	}
	
	private class MyThread extends Thread {
		@Override
		public void run() {
			U.println("thread start");
			for(int i = 0; i < 100000; i++) {
				incA();
			}
			U.println("thread stop");
		}
	}
	
	private static sun.misc.Unsafe UNSAFE;
	static {
		try {
			Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			UNSAFE = (sun.misc.Unsafe) f.get(null);
		} catch(Exception e) {
			U.println("unsafe error=" + e);
		}
	}
	
	static {
		try {
			aOffset = UNSAFE.objectFieldOffset(CASTest.class.getDeclaredField("a"));
		} catch(Exception e) {
			U.println("aOffset error=" + e);
			throw new Error(e);
		}
	}
}
