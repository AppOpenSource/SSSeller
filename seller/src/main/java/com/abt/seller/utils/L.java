package com.abt.seller.utils;

import android.util.Log;

/**
 * Log to Android logging system. Log message can be a string or a printf
 * formatted string with arguments. See
 * http://developer.android.com/reference/java/util/Formatter.html
 */
public class L {
	public static final int VERBOSE = Log.VERBOSE;
	public static final int DEBUG = Log.DEBUG;
	public static final int INFO = Log.INFO;
	public static final int WARN = Log.WARN;
	public static final int ERROR = Log.ERROR;
	private static final String TAG = "L";
	// Current log level
	public static int logLevel = Log.VERBOSE;

	/**
	 * Set the current log level.
	 */
	public static void setLogLevel(int logLevel) {
		L.logLevel = logLevel;
		Log.i(TAG, "Changing log level to " + logLevel);
	}

	/**
	 * Set the current log level.
	 */
	public static void setLogLevel(String logLevel) {
		if ("VERBOSE".equals(logLevel))
			L.logLevel = VERBOSE;
		else if ("DEBUG".equals(logLevel))
			L.logLevel = DEBUG;
		else if ("INFO".equals(logLevel))
			L.logLevel = INFO;
		else if ("WARN".equals(logLevel))
			L.logLevel = WARN;
		else if ("ERROR".equals(logLevel))
			L.logLevel = ERROR;
		Log.i(TAG, "Changing log level to " + L.logLevel + "(" + logLevel + ")");
	}

	/**
	 * Determine if log level will be logged
	 */
	public static boolean isLoggable(int logLevel) {
		return (L.logLevel >= logLevel);
	}

	/**
	 * Verbose log message.
	 */
	public static void v(String tag, String s) {
		if (L.VERBOSE < logLevel)
			return;
		Log.v(tag, s);
	}

	/**
	 * Debug log message.
	 */
	public static void d(String tag, String s) {
		if (L.DEBUG < logLevel)
			return;
		Log.d(tag, s);
	}

	/**
	 * Info log message.
	 */
	public static void i(String tag, String s) {
		if (L.INFO < logLevel)
			return;
		Log.i(tag, s);
	}

	/**
	 * Warning log message.
	 */
	public static void w(String tag, String s) {
		if (L.WARN < logLevel)
			return;
		Log.w(tag, s);
	}

	/**
	 * Error log message.
	 */
	public static void e(String tag, String s) {
		if (L.ERROR < logLevel)
			return;
		Log.e(tag, s);
	}

	/**
	 * Verbose log message.
	 */
	public static void v(String tag, String s, Throwable e) {
		if (L.VERBOSE < logLevel)
			return;
		Log.v(tag, s, e);
	}

	/**
	 * Debug log message.
	 */
	public static void d(String tag, String s, Throwable e) {
		if (L.DEBUG < logLevel)
			return;
		Log.d(tag, s, e);
	}

	/**
	 * Info log message.
	 */
	public static void i(String tag, String s, Throwable e) {
		if (L.INFO < logLevel)
			return;
		Log.i(tag, s, e);
	}

	/**
	 * Warning log message.
	 */
	public static void w(String tag, String s, Throwable e) {
		if (L.WARN < logLevel)
			return;
		Log.w(tag, s, e);
	}

	/**
	 * Error log message.
	 */
	public static void e(String tag, String s, Throwable e) {
		if (L.ERROR < logLevel)
			return;
		Log.e(tag, s, e);
	}

	/**
	 * Verbose log message with printf formatting.
	 */
	public static void v(String tag, String s, Object... args) {
		if (L.VERBOSE < logLevel)
			return;
		Log.v(tag, String.format(s, args));
	}

	/**
	 * Debug log message with printf formatting.
	 */
	public static void d(String tag, String s, Object... args) {
		if (L.DEBUG < logLevel)
			return;
		Log.d(tag, String.format(s, args));
	}

	/**
	 * Info log message with printf formatting.
	 */
	public static void i(String tag, String s, Object... args) {
		if (L.INFO < logLevel)
			return;
		Log.i(tag, String.format(s, args));
	}

	/**
	 * Warning log message with printf formatting.
	 */
	public static void w(String tag, String s, Object... args) {
		if (L.WARN < logLevel)
			return;
		Log.w(tag, String.format(s, args));
	}

	/**
	 * Error log message with printf formatting.
	 */
	public static void e(String tag, String s, Object... args) {
		if (L.ERROR < logLevel)
			return;
		Log.e(tag, String.format(s, args));
	}

	@SuppressWarnings("unused")
	private static boolean isFilteredLog(int level, String s) {
		if (s.contains("") && level == L.DEBUG)
			return true;
		return false;
	}
}