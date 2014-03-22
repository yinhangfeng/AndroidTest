package com.example.test;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Date;

public class ListFileTest {
	
	public static void test() {
		
		File dir = new File("javas");
		U.println("dir.lastModified" + dir.lastModified());
		U.println("dir.lastModified" + (new Date(dir.lastModified()).toString()));
		
	}
	
	private static void listNormal() {
		File dir = new File("javas");
		U.println("dir.exists " + dir.exists());
		long start = System.nanoTime();
		File[] files = dir.listFiles();
		U.println("file count=" + files.length);
		long fileLen, fileLastModified;
		for(int i = 0, len = files.length; i < len; i++) {
			fileLen = files[i].length();
			fileLastModified = files[i].lastModified();
		}
		long end = System.nanoTime();
		U.printTimeIntervalNano(start, end);
	}
	
	private static void listRef() {
		Field f;
		try {
			f = File.class.getDeclaredField("path");
			f.setAccessible(true);
		} catch(Exception e) {
			U.println("xxxx");
			return;
		}
		File dir = new File("javas");
		U.println("dir.exists " + dir.exists());
		long start = System.nanoTime();
		String[] files = dir.list();
		U.println("file count=" + files.length);
		long fileLen, fileLastModified;
		File file = new File("");
		try {
			for(int i = 0, len = files.length; i < len; i++) {
				f.set(file, files[i]);
				fileLen = file.length();
				fileLastModified = file.lastModified();
			}
		} catch(Exception e) {
			U.println("xxxx");
			return;
		}
		long end = System.nanoTime();
		U.printTimeIntervalNano(start, end);
	}

}
