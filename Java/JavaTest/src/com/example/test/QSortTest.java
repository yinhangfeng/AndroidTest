package com.example.test;

import java.util.Arrays;
import java.util.Random;

public class QSortTest {
	
	private static boolean less(int a, int b) {
		return a < b;
	}
	
	private static void exchange(int[] data, int i, int j) {
		int temp = data[i];
		data[i] = data[j];
		data[j] = temp;
	}
	
	public static void qSort1(int[] data, int start, int end) {
		if(start >= end) {
			return;
		}
		int partIndex = partition1(data, start, end);
		qSort1(data, start, partIndex - 1);
		qSort1(data, partIndex + 1, end);
	}

	private static int partition1(int[] data, int start, int end) {
		int pivot = data[start];
		int i = start;
		int j = end + 1;
		for(;;) {
			while(i < --j && less(pivot, data[j]));
			if(i == j) {
				break;
			}
			data[i] = data[j];
			while(++i < j && less(data[i], pivot));
			if(i == j) {
				break;
			}
			data[j] = data[i];
		}
		data[i] = pivot;
		return i;
	}
	
	public static void qSort2(int[] data, int start, int end) {
		if(start >= end) {
			return;
		}
		int partIndex = partition2(data, start, end);
		qSort2(data, start, partIndex - 1);
		qSort2(data, partIndex + 1, end);
	}
	
	private static int partition2(int[] data, int start, int end) {
		int i = start;
		int j = end + 1;
		for(;;) {
			while(++i < j && less(data[i], data[start]));
			while(i <= --j && less(data[start], data[j]));
			if(i >= j) {
				break;
			}
			exchange(data, i, j);
		}
		exchange(data, start, j);
		return j;
	}
	
	public static void qSort3(int[] data, int start, int end) {
		if(start >= end) {
			return;
		}
		int partIndex = partition3(data, start, end);
		qSort3(data, start, partIndex - 1);
		qSort3(data, partIndex + 1, end);
	}
	
	private static int partition3(int[] data, int start, int end) {
		int index = start;
		for (int i = start; i < end; ++i) {
			if (less(data[i], data[end])) {
				if(i != index) {
					exchange(data, index, i);
				}
				++index;
			}
		}
		exchange(data, index, end);
		return index;
	}
	
	public static void qSort3Way(int[] data, int start, int end) {
		if (start >= end) {
			return;
		}
		int lt = start;
		int gt = end;
		int i = start + 1;
		int pivotIndex = start;
		int pivot = data[pivotIndex];
		while (i <= gt) {
			if (less(data[i], pivot)) {
				exchange(data, i++, lt++);
			} else if (less(pivot, data[i])) {
				exchange(data, i, gt--);
			} else {
				i++;
			}
		}
		qSort3Way(data, start, lt - 1);
		qSort3Way(data, gt + 1, end);
	}
	
	public static void qSortDualPivot(int[] data, int lowIndex, int highIndex) {
		if (lowIndex >= highIndex) {
			return;
		}
		int pivot1 = data[lowIndex];
		int pivot2 = data[highIndex];
		if(pivot1 == pivot2) {
			int pivot1Index = lowIndex;
			do {
				if(++pivot1Index == highIndex) {
					return;
				}
			} while(data[pivot1Index] == pivot2);
			pivot1 = data[pivot1Index];
			exchange(data, lowIndex, pivot1Index);
		}
		if (pivot1 > pivot2) {
			pivot2 = data[lowIndex];
			pivot1 = data[highIndex];
			data[lowIndex] = pivot1;
			data[highIndex] = pivot2;
		}
		int lt = lowIndex + 1;
		int gt = highIndex - 1;
		for(int i = lowIndex + 1; i <= gt;) {
			if (less(data[i], pivot1)) {
				exchange(data, i++, lt++);
			} else if (less(pivot2, data[i])) {
				exchange(data, i, gt--);
			} else {
				i++;
			}
		}
		exchange(data, lowIndex, --lt);
		exchange(data, highIndex, ++gt);
		qSortDualPivot(data, lowIndex, lt - 1);
		qSortDualPivot(data, lt + 1, gt - 1);
		qSortDualPivot(data, gt + 1, highIndex);
	}
	
	public static void test() {
		int[] a = { 5, 9, 8, 99, 7, 10, 3, 2 };
		U.println(Arrays.toString(a));
		qSort3Way(a, 0, a.length - 1);
		U.println(Arrays.toString(a));
		int[] b = new int[20];
		Random random = new Random();
		for(int j = 1; j <= 20; ++j) {
			U.println("random test" + j);
			for(int i = 0, len = b.length; i < len; ++i) {
				b[i] = random.nextInt(j);
			}
			U.println(Arrays.toString(b));
			qSort3Way(b, 0, b.length - 1);
			U.println(Arrays.toString(b));
		}
	}

}
