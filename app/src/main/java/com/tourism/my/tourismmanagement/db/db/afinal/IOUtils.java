package com.tourism.my.tourismmanagement.db.db.afinal;

import android.database.Cursor;

import java.io.Closeable;

/**
 * 关闭各种资源链接
 * 
 * @author kaka
 * 
 */
public class IOUtils {

	private IOUtils() {
	}

	public static void closeQuietly(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Throwable e) {
			}
		}
	}

	public static void closeQuietly(Cursor cursor) {
		if (cursor != null) {
			try {
				cursor.close();
			} catch (Throwable e) {
			}
		}
	}
}
