package com.abt.seller.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import android.os.Environment;

public class SDCardUtil {

	public static File getSDDir() {
		return Environment.getExternalStorageDirectory();
	}
	
	public static boolean isSDCardAvailable() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}
	
	public static boolean isFileExistsInSDCard(String filePath, String fileName) {
		boolean flag = false;
		if (isSDCardAvailable()) {
			File file = new File(filePath, fileName);
			if (file.exists()) {
				flag = true;
			}
		}
		return flag;
	}

	public static boolean saveFileToSDCard(String filePath, String filename, String content)
			throws Exception {
		boolean flag = false;
		if (isSDCardAvailable()) {
			File dir = new File(filePath);
			if (!dir.exists()) {
				dir.mkdir();
			}
			File file = new File(filePath, filename);
			FileOutputStream outStream = new FileOutputStream(file);
			outStream.write(content.getBytes());
			outStream.close();
			flag = true;
		}
		return flag;
	}

	public static byte[] readFileFromSDCard(String filePath, String fileName) {
		byte[] buffer = null;
		try {
			if (isSDCardAvailable()) {
				String filePaht = filePath + "/" + fileName;
				FileInputStream fin = new FileInputStream(filePaht);
				int length = fin.available();
				buffer = new byte[length];
				fin.read(buffer);
				fin.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer;
	}

	public static boolean deleteSDFile(String filePath, String fileName) {
		File file = new File(filePath + "/" + fileName);
		if (file == null || !file.exists() || file.isDirectory())
			return false;
		return file.delete();
	}
}