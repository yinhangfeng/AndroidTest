package com.example.shapelibtest;

import com.example.shapelibtest.ShapeFile.GeometryType;

/**
 * ShapeFile读取的几何对象
 * @author 殷杭锋
 */
public class Shape {
	static final String TAG = "Shape";
	
	private int type = GeometryType.UNKNOWN;
	private int vertices = 0;
	private double[] xArr;
	private double[] yArr;
	
	public int getVertices() {
		return vertices;
	}
	
	public double getX(int index) {
		return xArr[index];
	}
	
	public double getY(int index) {
		return yArr[index];
	}
}
