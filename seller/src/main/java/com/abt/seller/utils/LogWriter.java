package com.abt.seller.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogWriter {
	private static LogWriter mLogWriter;
	private static String mPath;
	private static Writer mWriter;
	private static SimpleDateFormat df = new SimpleDateFormat("[yy-MM-dd hh:mm:ss]: ");
	
	public LogWriter() { }
	
	private LogWriter(String file_path) {
		LogWriter.mPath = file_path;
		LogWriter.mWriter = null;
	}

	public static LogWriter open(String file_path) {
		if (mLogWriter == null) {
			mLogWriter = new LogWriter(file_path);
		}
		try {
			mWriter = new BufferedWriter(new FileWriter(mPath, true), 2048);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mLogWriter;
	}
	
	public void close() {
		try {
			mWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void print(String log) {
		try {
			if (mWriter == null) {
				return;
			}
			mWriter.write(df.format(new Date()));
			mWriter.write(log);
			mWriter.write("\n");
			mWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void print(Class<?> cls, String log) {
		try {
			mWriter.write(df.format(new Date()));
			mWriter.write(cls.getSimpleName() + " ");
			mWriter.write(log);
			mWriter.write("\n");
			mWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}