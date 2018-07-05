package com.abt.seller.utils;

import java.util.HashMap;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public final class SharedPreferenceUtil {
	private static final String PREFERENCE_NAME = "sharedpreference";

	@SuppressWarnings("deprecation")
	@SuppressLint("WorldWriteableFiles")
	public static SharedPreferences getSharedPreferences(Context cx) {
		return cx.getSharedPreferences(PREFERENCE_NAME,	Context.MODE_WORLD_WRITEABLE);
	}

	public static boolean saveStringValue(Context cx, String key, String value) {
		SharedPreferences sharedPreferences = getSharedPreferences(cx);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		return editor.commit();
	}

	public static String getStringValue(Context cx, String key) {
		SharedPreferences sharedPreferences = getSharedPreferences(cx);
		return sharedPreferences.getString(key, "");
	}

	public static boolean saveStringValues(Context context,
			HashMap<String, String> valuesMap) {
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		String value = "";
		for (String key : valuesMap.keySet()) {
			value = valuesMap.get(key);
			editor.putString(key, value);
		}
		return editor.commit();
	}

	public static boolean saveBooleanValue(Context context, String key,
			boolean value) {
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}

	public static boolean getBooleanValue(Context context, String key,
			boolean defaultValue) {
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		return sharedPreferences.getBoolean(key, defaultValue);
	}

	public static void cleanSharedPreference(Context context) {
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}

	public static boolean saveIntValue(Context context, String key, int value) {
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		return editor.commit();
	}

	public static int getIntValue(Context context, String key, int defaultValue) {
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		return sharedPreferences.getInt(key, defaultValue);
	}

	public static boolean saveFloatValue(Context context, String key,
			float value) {
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		Editor editor = sharedPreferences.edit();
		editor.putFloat(key, value);
		return editor.commit();
	}

	public static float getFloatValue(Context context, String key,
			float defaultValue) {
		SharedPreferences sharedPreferences = getSharedPreferences(context);
		return sharedPreferences.getFloat(key, defaultValue);
	}
}