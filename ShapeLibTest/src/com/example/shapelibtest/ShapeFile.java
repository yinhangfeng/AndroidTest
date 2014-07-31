package com.example.shapelibtest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;
import android.util.Log;

/**
 * .shp .dbf .shx文件格式读写类
 * @author 殷杭锋
 */
public class ShapeFile {
	static final String TAG = "ShapeFile";
	/**
	 * 几何类型 数值与native shapelib对应
	 */
	public interface GeometryType {
		final int UNKNOWN = 0; //SHPT_NULL 或 >5
		final int POINT = 1;//SHPT_POINT
		final int POLYLINE = 3;//SHPT_ARC
		final int POLYGON = 5;//SHPT_POLYGON
	}
	
	//shp文件存储目录
	private File path;
	//shp文件名称不带扩展名
	private String fileName;
	//shp目录加名称(不带扩展名)
	private String shpPath;

	//SHPHandle指针
	private int mNativeSHPHandle = 0;
	//DBFHandle指针
	private int mNativeDBFHandle = 0;
	//shape数
	private int mEntities = -1;
	//几何类型
	private int mGeometryType = GeometryType.UNKNOWN;
	
	private ArrayList<DbfField> fieldList;
	//fieldName => fieldIndex
	private HashMap<String, Integer> nameIndexMap;

	/**
	 * shp文件必须存在
	 * @param path shp文件存储目录
	 * @param fileName shp文件名称不带扩展名
	 */
	public ShapeFile(File path, String fileName) {
		if(path == null || TextUtils.isEmpty(fileName)) {
			throw new RuntimeException("shpPath == null || TextUtils.isEmpty(fileName)");
		}
		setPath(path, fileName);
		if(!path.exists()) {
			Log.e(TAG, "!path.exists()");
			return;
		}
		if(new File(path, fileName + ".shp").exists() || new File(path, fileName + ".SHP").exists()) {
			openShp(shpPath);
		}
	}
	
	/**
	 * 创建shp dbf文件
	 */
	private ShapeFile(File path, String fileName, int geometryType, ArrayList<DbfField> fieldList) {
		setPath(path, fileName);
		this.mGeometryType = geometryType;
		setFieldList(fieldList);
		createShp(shpPath, geometryType, fieldList);
	}
	
	private void setPath(File path, String fileName) {
		this.path = path;
		this.fileName = fileName;
		shpPath = path.getAbsolutePath() + "/" + fileName;
	}
	
	private void setFieldList(ArrayList<DbfField> fieldList) {
		this.fieldList = fieldList;
		nameIndexMap = new HashMap<String, Integer>();
		for(int i = 0, len = fieldList.size(); i < len; ++i) {
			nameIndexMap.put(fieldList.get(i).name, i);
		}
	}
	
	/**
	 * 初始化mNativeSHPHandle与mNativeDBFHandle 如果文件存在
	 */
	private void openShp(String shpPath) {
		mNativeSHPHandle = nativeSHPOpen(shpPath);
		mNativeDBFHandle = nativeDBFOpen(shpPath);
		if(mNativeSHPHandle != 0) {
			mEntities = nativeGetEntities(mNativeSHPHandle);
		}
		if(mNativeDBFHandle != 0) {
			mGeometryType = nativeGetShapeType(mNativeDBFHandle);
			if(mGeometryType > 5) {
				mGeometryType = GeometryType.UNKNOWN;
			}
		}
		if(mNativeSHPHandle == 0) {
			Log.e(TAG, "nativeSHPOpen faild");
		}
		if(mNativeDBFHandle == 0) {
			Log.e(TAG, "nativeDBFOpen faild");
			return;
		}
		ArrayList<DbfField> list = new ArrayList<DbfField>();
		for(int i = 0, len = nativeDBFGetFieldCount(mNativeDBFHandle); i < len; ++i) {
			list.add(nativeGetField(mNativeDBFHandle, i));
		}
		setFieldList(list);
	}
	
	private void createShp(String shpPath, int geometryType, ArrayList<DbfField> fieldList) {
		mNativeSHPHandle = nativeSHPCreate(shpPath, mGeometryType);
		mNativeDBFHandle = nativeDBFCreate(shpPath);
		if(mNativeSHPHandle == 0) {
			Log.e(TAG, "nativeSHPCreate faild");
		}
		if(mNativeDBFHandle == 0) {
			Log.e(TAG, "nativeDBFCreate faild");
			return;
		}
		for(DbfField field : fieldList) {
			nativeDBFAddField(mNativeDBFHandle, field.name, field.width, field.decimals);
		}
	}
	
	/**
	 * 获取field列表
	 * @return null dbf不存在
	 */
	public List<DbfField> getFields() {
		return fieldList;
	}
	
	/**
	 * 获取一个几何
	 * @param index 几何序号
	 */
	public Shape getShape(int index) {
		if(mNativeSHPHandle == 0) {
			return null;
		}
		return nativeGetShape(mNativeSHPHandle, index);
	}
	
	/**
	 * 获取属性Map
	 * @param index 属性序号
	 */
	public Map<String, Object> getAttributes(int index) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(mNativeDBFHandle == 0) {
			return map;
		}
		DbfField field;
		for(int i = 0, len = fieldList.size(); i < len; ++i) {
			field = fieldList.get(i);
			switch(field.type) {
			case DbfField.FTINTEGER:
				map.put(field.name, nativeDBFReadIntegerAttribute(mNativeDBFHandle, index, i));
				break;
			case DbfField.FTDOUBLE:
				map.put(field.name, nativeDBFReadDoubleAttribute(mNativeDBFHandle, index, i));
				break;
			default:
				map.put(field.name, nativeDBFReadStringAttribute(mNativeDBFHandle, index, i));
				break;
			}
		}
		return map;
	}
	
	/**
	 * 构造并创建shap
	 */
	public static class Builder {
		
		private File path;
		private String name;
		private int mGeometryType = GeometryType.UNKNOWN;
		private ArrayList<DbfField> fieldList;
		
		public Builder() {
			fieldList = new ArrayList<DbfField>();
		}
		
		public Builder setPath(File path) {
			if(path == null) {
				throw new IllegalArgumentException("path == null");
			}
			this.path = path;
			return this;
		}
		
		public Builder setName(String name) {
			this.name = name;
			return this;
		}
		
		public Builder setGeometryType(int geometryType) {
			if(geometryType != GeometryType.POINT && geometryType != GeometryType.POLYLINE && geometryType != GeometryType.POLYGON) {
				throw new RuntimeException("geometryType必须为POINT、POLYLINE、POLYGON");
			}
			mGeometryType = geometryType;
			return this;
		}
		
		public Builder addField(DbfField field) {
			fieldList.add(field);
			return this;
		}
		
		public Builder addField(String name, int type) {
			fieldList.add(new DbfField(name, type));
			return this;
		}
		
		public ShapeFile create() {
			if(path == null) {
				throw new RuntimeException("未设置path");
			}
			if(fieldList.isEmpty()) {
				throw new RuntimeException("未添加Field");
			}
			if(mGeometryType == GeometryType.UNKNOWN) {
				throw new RuntimeException("未设置GeometryType");
			}
			if(name == null) {
				name = "xxx";
			}
			return new ShapeFile(path, name, mGeometryType, fieldList);
		}
		
	}

	public static class DbfField {
		public static final int FTSTRING = 0;
		public static final int FTINTEGER = 1;
		public static final int FTDOUBLE = 2;
		
		public String name;
		public int type;
		/**
		 * The width of the field to be created.  For FTString fields this
		 * establishes the maximum length of string that can be stored.
		 * For FTInteger this establishes the number of digits of the
		 * largest number that can
		 * be represented.  For FTDouble fields this in combination
		 * with the nDecimals value establish the size, and precision
		 * of the created field
		 */
		public int width;
		/**
		 * The number of decimal places to reserve for FTDouble fields.
		 * For all other field types this should be zero.  For instance
		 * with nWidth=7, and nDecimals=3 numbers would be formatted
		 * similarly to `123.456'
		 */
		public int decimals;
		
		public DbfField(String name, int type) {
			int width = 0;
			int decimals = 0;
			if(type == FTINTEGER) {
				width = 1;
			} else if(type == FTDOUBLE) {
				width = decimals = 8;
			} else if(type == FTSTRING) {
				width = 128;
			} else {
				throw new IllegalArgumentException("不支持的类型");
			}
			this.name = name;
			this.type = type;
			this.width = width;
			this.decimals = decimals;
		}
		
		public DbfField(String name, int type, int width, int decimals) {
			this.name = name;
			this.type = type;
			this.width = width;
			this.decimals = decimals;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if(mNativeSHPHandle != 0 || mNativeDBFHandle != 0) {
			nativeRelease();
		}
	}

	private native void nativeInit();
	private native void nativeRelease();
	private native int nativeSHPOpen(String shpPath);
	private native int nativeDBFOpen(String shpPath);
	private native int nativeGetEntities(int nativeSHPHandle);
	private native int nativeGetShapeType(int nativeSHPHandle);
	private native int nativeSHPCreate(String shpPath, int shapeType);
	private native int nativeDBFCreate(String shpPath);
	private native void nativeDBFAddField(int nativeDBFHandle, String fieldName, int width, int decimals);
	private native int nativeDBFGetFieldCount(int nativeDBFHandle);
	private native DbfField nativeGetField(int nativeDBFHandle, int fieldIndex);
	private native Shape nativeGetShape(int nativeSHPHandle, int shpIndex);
	private native String nativeDBFReadStringAttribute(int nativeDBFHandle, int dbfIndex, int fieldIndex);
	private native int nativeDBFReadIntegerAttribute(int nativeDBFHandle, int dbfIndex, int fieldIndex);
	private native double nativeDBFReadDoubleAttribute(int nativeDBFHandle, int dbfIndex, int fieldIndex);

}
