package com.example.test;

public class StringHashCodeTest {
	
	private static String s = "http://img1.mydrivers.com/img/topimg/20140319/092416699.jpg";
	private static char[] value = s.toCharArray();
	private static int hash = 0;
	
	public static void test() {
		String s = StringHashCodeTest.s;
		s.hashCode();
		long start, end;
		
		start = System.nanoTime();
		for(int i = 0; i < 10000; ++i) {
			s.hashCode();
		}
		end = System.nanoTime();
		U.printTimeIntervalNano(start, end);
		
		start = System.nanoTime();
		for(int i = 0; i < 10000; ++i) {
			hashcode();
		}
		end = System.nanoTime();
		U.printTimeIntervalNano(start, end);
		
		start = System.nanoTime();
		for(int i = 0; i < 10000; ++i) {
			s.hashCode();
		}
		end = System.nanoTime();
		U.printTimeIntervalNano(start, end);
		
		start = System.nanoTime();
		for(int i = 0; i < 10000; ++i) {
			hashcode();
		}
		end = System.nanoTime();
		U.printTimeIntervalNano(start, end);

	}
	
	private static int hashcode() {
		int h = hash;
        if (h == 0 && value.length > 0) {
            char val[] = value;

            for (int i = 0; i < value.length; i++) {
                h = 31 * h + val[i];
            }
            hash = 0;
        }
        return h;
	}

}
