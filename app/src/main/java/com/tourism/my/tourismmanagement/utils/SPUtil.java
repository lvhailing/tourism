package com.tourism.my.tourismmanagement.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SPUtil {
	public static void save(Context context, String key, String value) {
		SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String get(Context context, String key) {
		SharedPreferences preferences=context.getSharedPreferences("user", Context.MODE_PRIVATE);
		return preferences.getString(key, "");
	}
}
