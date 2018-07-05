package com.abt.seller.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static final String DATE_FORMAT_PATTERN_1 = "yyyy-MM-dd HH:mm";
	private static final String DATE_FORMAT_PATTERN_2 = "HH:mm";

	public static String getCurrDate() {
		Calendar c = Calendar.getInstance();
		Date date = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);
		String dStr = sdf.format(date);
		return dStr;
	}
	
	public static String getCurDate() {
		Calendar c = Calendar.getInstance();
		String year = String.valueOf(c.get(Calendar.YEAR));
		String month = String.valueOf(c.get(Calendar.MONTH));
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH) + 1);
		String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		String mins = String.valueOf(c.get(Calendar.MINUTE));
		StringBuilder sb = new StringBuilder();
		sb.append(year + "-" + month + "-" + day + " " + hour + ":" + mins);
		return sb.toString();
	}

	public static String getDate(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);
		String dStr = sdf.format(new Date(time));
		return dStr;
	}
	
	public static String getFormatedDate(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);
		String dStr = sdf.format(new Date(1000 * time));
		return dStr;
	}
	
	public static String getFormatedDate(String time) {
		long t = Long.parseLong(time);
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);
		String dStr = sdf.format(new Date(1000 * t));
		return dStr;
	}

	public static String getFormatedDate_1(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN_1);
		String dStr = sdf.format(new Date(1000 * time));
		return dStr;
	}

	public static String getHumanReadiableDate(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN_2);
		String dStr = sdf.format(new Date(time));
		return dStr;
	}
	
	public static String getCurrDateByPattern(String pattern) {
		Calendar c = Calendar.getInstance();
		Date date = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String dStr = sdf.format(date);
		return dStr;
	}

	public static String formatTime(long t) {
		int hour = 0;
		int min = 0;
		int sec = 0;
		String formatedTime = "";

		if (t > 60 * 60 * 1000) {
			hour = (int) (t / (60 * 60 * 1000));
			t -= hour * 60 * 60 * 1000;
		}
		if (t > 60 * 1000) {
			min = (int) (t / (60 * 1000));
			t -= min * 60 * 1000;
		}
		if (t > 1000) {
			sec = (int) (t / 1000);
			t -= sec * 1000;
		}

		if (hour >= 10) {
			formatedTime += hour + ":";
		} else if (hour > 0 && hour < 10) {
			formatedTime += "0" + hour + ":";
		}
		
		if (min >= 10 && min < 60) {
			formatedTime += min + ":";
		} else if (min > 0 && min < 10) {
			formatedTime += "0" + min + ":";
		} else {
			formatedTime += "00" + ":";
		}

		if (sec >= 10 && sec < 60) {
			formatedTime += sec + "";
		} else if (sec > 0 && sec < 10) {
			formatedTime += "0" + sec;
		} else {
			formatedTime += "00";
		}
		return formatedTime;
	}
}