package com.abt.seller.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class SystemUtil {
	
	@SuppressWarnings("deprecation")
	public static int getWidth(Context cx) {
		WindowManager wm = ((Activity) cx).getWindowManager();
		return wm.getDefaultDisplay().getWidth();
	}
	
	@SuppressWarnings("deprecation")
	public static int getHeight(Context cx) {
		WindowManager wm = ((Activity) cx).getWindowManager();
		return wm.getDefaultDisplay().getHeight();
	}
	
	public static String getCurTimeByFormat(String format) {
		SimpleDateFormat dateformat = new SimpleDateFormat(format);
		String time = dateformat.format(new Date());
		return time;
	}

	/**
	 * 返回当前日期之后或之前几天的日期
	 * day为正整数则往后推，为负整数则往前推
	 */
	@SuppressWarnings("static-access")
	public static String getDateAfter(int day) {
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, day);
		date = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		return dateString;
	}

	public static boolean isNetworkAvailable(Context cx) {
		ConnectivityManager cm = (ConnectivityManager) cx.getSystemService(
				Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info == null || !info.isAvailable()) {
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean isNetworkConnected(Context cx) {
		ConnectivityManager cm = (ConnectivityManager) cx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info.isRoaming()) {
			return true;
		}
		return true;
	}
	
	public static boolean isWifiConnected(Context cx) {
		ConnectivityManager cm = (ConnectivityManager) cx.getSystemService(
				Activity.CONNECTIVITY_SERVICE);
		boolean wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).
				isConnectedOrConnecting();
		if (wifi) {
			return true;
		}
		return false;
	}

	/**
	 * Toggle keyboard, if the keyboard is visible, then hidden it, if it's
	 * invisible, then show it
	 */
	public static void toggleKeyboard(Context cx) {
		InputMethodManager imm = (InputMethodManager) cx.getSystemService(
				Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public static boolean isServiceRunning(Context cx, String className) {
		ActivityManager am = (ActivityManager) cx.getSystemService(
				Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> list = am.getRunningServices(30);
		for (RunningServiceInfo info : list) {
			if (info.service.getClassName().equals(className)) {
				return true;
			}
		}
		return false;
	}
}