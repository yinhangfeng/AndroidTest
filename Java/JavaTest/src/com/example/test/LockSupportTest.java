package com.example.test;

import java.util.concurrent.locks.LockSupport;

public class LockSupportTest {

	public static void test() {
		MyThread thread = new MyThread();
		print("main thread.start");
		thread.start();
		thread.interrupt();
		try {
			Thread.sleep(3000);
		} catch(Exception e) {
			
		}
	//	print(LockSupport.getBlocker(thread).toString());
		print("main LockSupport.unpark(thread)");
		LockSupport.unpark(thread);
		try {
			Thread.sleep(3000);
		} catch(Exception e) {
			
		}
		thread.interrupt();
	}
	
	private static class MyThread extends Thread {
		
		@Override
		public void run() {
			print("MyThread start");
			LockSupport.park();
			LockSupport.park();
			print("MyThread try");
			try {
				for (int i = 0;; i++) {
					Thread.sleep(500);
					print("i=" + i);

				}
			} catch (InterruptedException e) {
				print("MyThread InterruptedException");
			}
			print("MyThread end");
		}
	}
	
	private static void print(String msg) {
		System.out.println(msg);
	}
}
