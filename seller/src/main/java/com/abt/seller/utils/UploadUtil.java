package com.abt.seller.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.abt.seller.R;
import com.abt.seller.models.Constant;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class UploadUtil {
	private static final String BOUNDARY = UUID.randomUUID().toString();
	private static UploadUtil uploadUtil;
	private Context context;
	private OnUploadProcessListener onUploadProcessListener;
	
	private UploadUtil(Context Context) {
		this.context = (Activity) context;
	}

	public static UploadUtil getInstance(Context context) {
		if (uploadUtil == null) {
			uploadUtil = new UploadUtil(context);
		}
		return uploadUtil;
	}

	public static interface OnUploadProcessListener {
		void onUploadDone(int responseCode, String message);

		void onUploadProcess(int uploadSize);

		void initUpload(int fileSize);
	}

	public void setOnUploadProcessListener(
			OnUploadProcessListener onUploadProcessListener) {
		this.onUploadProcessListener = onUploadProcessListener;
	}

	public void uploadImage(final File file, final String fileKey,
			final String RequestURL, final Map<String, String> param) {
		if (file == null || (!file.exists())) {
			sendMessage(Constant.UPLOAD_FILE_NOT_EXISTS_CODE, context
					.getResources().getString(R.string.file_not_exit));
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				toUploadImage(file, fileKey, RequestURL, param);
			}
		}).start();
	}

	private void toUploadImage(File file, String fileKey, String RequestURL,
			Map<String, String> param) {
		try {
			URL url = new URL(RequestURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(Constant.readTimeOut);
			con.setConnectTimeout(Constant.connectTimeout);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Charset", "utf-8");
			con.setRequestProperty("connection", "keep-alive");
			con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			con.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			DataOutputStream dos = new DataOutputStream(con.getOutputStream());

			if (param != null && param.size() > 0) {
				Iterator<String> it = param.keySet().iterator();
				while (it.hasNext()) {
					StringBuffer sbf = new StringBuffer();
					String key = it.next();
					String value = param.get(key);
					sbf.append("--").append(BOUNDARY).append("\r\n");
					sbf.append("Content-Disposition: form-data; name=\"")
							.append(key).append("\"").append("\r\n")
							.append("\r\n");
					sbf.append(value).append("\r\n");
					dos.write(sbf.toString().getBytes());
				}
			}

			StringBuffer sb = new StringBuffer();
			sb.append("--").append(BOUNDARY).append("\r\n");
			sb.append("Content-Disposition:form-data; name=\"" + fileKey
					+ "\"; filename=\"" + file.getName() + "\"" + "\r\n");
			sb.append("Content-Type:image/pjpeg" + "\r\n");
			sb.append("\r\n");
			dos.write(sb.toString().getBytes());

			InputStream is = new FileInputStream(file);
			byte[] bytes = new byte[1024];
			int len = 0;
			while ((len = is.read(bytes)) != -1) {
				dos.write(bytes, 0, len);
			}
			is.close();
			dos.write("\r\n".getBytes());
			byte[] end_data = ("--" + BOUNDARY + "--" + "\r\n").getBytes();
			dos.write(end_data);
			dos.flush();

			int res = con.getResponseCode();
			Log.e("TAG", "response code:" + res);
			if (res == 200) {
				InputStream input = con.getInputStream();
				StringBuffer resSB = new StringBuffer();
				int ss;
				while ((ss = input.read()) != -1) {
					resSB.append((char) ss);
				}
				sendMessage(Constant.UPLOAD_SUCCESS_CODE, resSB.toString());
				return;
			} else {
				sendMessage(Constant.UPLOAD_SERVER_ERROR_CODE, "上传失败：code="
						+ res);
				return;
			}
		} catch (MalformedURLException e) {
			sendMessage(Constant.UPLOAD_SERVER_ERROR_CODE,
					"上传失败：error=" + e.getMessage());
			e.printStackTrace();
			return;
		} catch (IOException e) {
			sendMessage(Constant.UPLOAD_SERVER_ERROR_CODE,
					"上传失败：error=" + e.getMessage());
			e.printStackTrace();
			return;
		}
	}

	public void uploadImages(final String requestURL,
			final List<String> imagesList) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				toUploadImages(requestURL, imagesList);
			}
		}).start();
	}

	private void toUploadImages(String requestURL, List<String> imagesList) {
		try {
			String BOUNDARY = "------------------------7dc2fd5c0894";
			byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();

			URL url = new URL(requestURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("connection", "Keep-Alive");
			con.setRequestProperty("Charsert", "UTF-8");
			con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			con.setRequestProperty("user-agent",
					"Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0)");
			OutputStream out = new DataOutputStream(con.getOutputStream());

			StringBuffer params = new StringBuffer();
			params.append("--" + BOUNDARY + "\r\n");
			params.append("Content-Disposition: form-data; name=\"name\"\r\n\r\n");
			params.append("name"); // to be alive
			params.append("\r\n");
			params.append("--" + BOUNDARY + "\r\n");
			params.append("Content-Disposition: form-data; name=\"address\"\r\n\r\n");
			params.append("address"); // to be alive
			params.append("\r\n");

			out.write(params.toString().getBytes());
			for (int i = 0; i < imagesList.size(); i++) {
				File file = new File(imagesList.get(i));
				StringBuilder sb = new StringBuilder();
				sb.append("--");
				sb.append(BOUNDARY);
				sb.append("\r\n");
				sb.append("Content-Disposition: form-data; name=\"img[]\"; filename=\""
						+ file.getName() + "\"\r\n");
				sb.append("Content-Type:image/pjpeg\r\n\r\n");
				out.write(sb.toString().getBytes());

				DataInputStream in = new DataInputStream(new FileInputStream(file));
				int bytes = 0;
				byte[] bufferOut = new byte[1024];
				while ((bytes = in.read(bufferOut)) != -1) {
					out.write(bufferOut, 0, bytes);
				}
				out.write("\r\n".getBytes());
				in.close();
			}
			out.write(end_data);
			out.flush();
			out.close();
			
			InputStreamReader isr = new InputStreamReader(con.getInputStream());
			BufferedReader reader = new BufferedReader(isr);
			String line = null;
			while ((line = reader.readLine()) != null) {
				Log.d("TAG", "line : " + line);
				sendMessage(Constant.UPLOAD_SUCCESS_CODE, line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendMessage(int responseCode, String responseMessage) {
		onUploadProcessListener.onUploadDone(responseCode, responseMessage);
		Log.d("TAG", "hhhhhhhhhhhhhhhhhhhhhhhhhhh : " + responseMessage);
	}
}