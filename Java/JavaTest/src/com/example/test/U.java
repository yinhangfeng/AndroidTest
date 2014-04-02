package com.example.test;

public class U {
	
	public static void println(Object obj) {
		System.out.println(obj.toString());
	}
	
	public static void printTimeIntervalNano(long start, long end) {
		println("time interval " + (end - start) + "ns" + " " + ((end - start) / 1000 / 1000) + "ms");
	}

}
