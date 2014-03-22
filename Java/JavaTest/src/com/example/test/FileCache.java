package com.example.test;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class FileCache {
	public static final long MAX_FILE_CACHE_SIZE = 32 * 1024 * 1024;
	private static final long CLEAN_TARGET_SIZE = MAX_FILE_CACHE_SIZE * 4 / 5;
	private static final String INFO_FILE_NAME = ".info";
	private static final long CLEAN_INTERVAL = 3 * 1000;
	private File cacheDir;
	private int a = 0;
	
	public FileCache(File cacheDir) {
		this.cacheDir = cacheDir;
	}
	
	public static void test() {
		FileCache cache = new FileCache(new File("javas"));
		cache.cleanCache();
	}
	
	private void checkAndClean() {
		U.println("checkAndClean");
		long start = System.nanoTime();
		File[] files = cacheDir.listFiles();
		if(files == null) {
			return;
		}
		long totalSize = 0;
		for(File file : files) {
			totalSize += file.length();
		}
		U.println("totalSize = " + (totalSize / 1024) + "KB MAX_FILE_CACHE_SIZE=" + (MAX_FILE_CACHE_SIZE / 1024) + " KB");
		if(totalSize < MAX_FILE_CACHE_SIZE) {
			U.println("xxxx");
			return;
		}
//		Integer[] aa = new Integer[files.length];
//		for(int i = 0, len = aa.length; i < len; ++i) {
//			aa[i] = (int) files[i].lastModified();
//		}
//		Arrays.sort(aa, new Comparator<Integer>() {
//
//			@Override
//			public int compare(Integer o1, Integer o2) {
//				return o1 - o2;
//			}
//			
//		});
		Arrays.sort(files, new Comparator<File>() {

			@Override
			public int compare(File lhs, File rhs) {
				a++;
				return (int) (lhs.lastModified() - rhs.lastModified());
			}
			
		});
		for(int i = 0, len = files.length; totalSize > CLEAN_TARGET_SIZE && i < len; ++i) {
			File file = files[i];
			totalSize -= file.length();
			file.delete();
			//U.println("totalSize= " + totalSize);
		}
		long end = System.nanoTime();
		U.printTimeIntervalNano(start, end);
		U.println("a=" + a);
	}
	
	private void setNewCleanTime() {
		U.println("setNewCleanTime");
		File info = new File(cacheDir, INFO_FILE_NAME);
		if(!info.exists()) {
			try {
				info.createNewFile();
			} catch(Exception ig) {	
			}
		}
		info.setLastModified(System.currentTimeMillis());
	}
	
	public void cleanCache() {
		File info = new File(cacheDir, INFO_FILE_NAME);
		long lastClean = info.lastModified();
		if(System.currentTimeMillis() - lastClean < CLEAN_INTERVAL) {
			U.println("111");
			return;
		}
		U.println("222");
		new Thread() {
			@Override
			public void run() {
				checkAndClean();
				setNewCleanTime();
			}
		}.start();
		
	}

}
