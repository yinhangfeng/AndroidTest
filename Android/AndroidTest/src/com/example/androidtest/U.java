package com.example.androidtest;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

public class U {
	
	/**
	 * bitmap大小
	 */
	public static int getBitmapSize(Bitmap bm) {
		return bm.getRowBytes() * bm.getHeight();
	}

	/**
	 * Drawable -> Bitmap
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap.createBitmap(
		drawable.getIntrinsicWidth(),
		drawable.getIntrinsicHeight(),
		drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 从资源中获取Bitmap
	 */
	public static Bitmap getBitmapFromResources(Context context, int resourceId) {
		return BitmapFactory.decodeResource(context.getResources(), resourceId);
	}

	/**
	 * Bitmap -> byte[]
	 */
	public static byte[] bitmapToBytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * byte[] → Bitmap
	 */
	public static Bitmap bytesToBimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		}
		return null;

	}

}
