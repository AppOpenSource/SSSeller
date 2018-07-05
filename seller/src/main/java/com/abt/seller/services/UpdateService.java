package com.abt.seller.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import com.abt.seller.models.Constant;
import com.abt.seller.utils.HttpUtil;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

public class UpdateService extends BaseHttpService {
	private int serverVerCode = 1;
	private String updateUrl = "";
	private ProgressDialog pBar;
	private Handler handler = new Handler();

	public UpdateService(Context context) {
		super(context);
	}

	public void startUpdate() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				checkUpdate();
				Looper.loop();
			}
		}).start();
	}
	
	private void checkUpdate() {
		if (getServerVerCode()) {
			if (serverVerCode > getLocalVerCode(context)) {
				doNewVersionUpdate();
			} else {
				notNewVersionShow();
			}
		}
	}
	
	private boolean getServerVerCode() {
		try {
			JSONObject jo = new JSONObject();
			jo.put("platform", "android");
			jo.put("versionName", getLocalVerCode(context));
			jo.put("client", "shop");
			String res = HttpUtil.doPost(Constant.UPDATE_SERVER, "[[\"parm\"," + jo.toString() + "]]");
			return parseRes(res);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean parseRes(String res) {
		try {
			JSONObject obj = new JSONObject(res);
			if (obj.getInt("res") != 0) {
				return false;
			}
			serverVerCode = (int) obj.getDouble("versionName");
			updateUrl = obj.getString("updateUrl");
//			String platform = obj.getString("platform");
//			String client = obj.getString("client");
//			String size = obj.getString("size");
//			String updateTime = obj.getString("updateTime");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void notNewVersionShow() {
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本:");
		sb.append(getLocalVerName(context));
		sb.append(",\n已是最新版,无需更新!");
		new AlertDialog.Builder(context)
			.setTitle("软件更新")
			.setMessage(sb.toString())
			.setPositiveButton("确定", cancelClick).show();
	}
	
	private void doNewVersionUpdate() {
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本:");
		sb.append(getLocalVerName(context));
		sb.append(", 发现新版本:");
		sb.append(serverVerCode);
		sb.append(", 是否更新?");
		new AlertDialog.Builder(context)
			.setTitle("软件更新")
			.setMessage(sb.toString())
			.setPositiveButton("更新", updateClick)
			.setNegativeButton("暂不更新", cancelClick).show();
	}
	
	private OnClickListener updateClick = new OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			pBar = new ProgressDialog(context);
			pBar.setTitle("正在下载");
			pBar.setMessage("请稍候...");
			pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pBar.show();
			downAndUpdate(updateUrl);
		}
	};
	
	private OnClickListener cancelClick = new OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			dialog.dismiss();
		}
	};

	private void downAndUpdate(final String url) {
		new Thread() {
			public void run() {
				downForUpdate(url);
			}
		}.start();
	}
	
	private void downForUpdate(String url) {
		try {
			getApkFile(url);
			doUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	private void getApkFile(String url) throws Exception {
		DefaultHttpClient dhc = new DefaultHttpClient();
		HttpResponse response = dhc.execute(new HttpGet(url));
		InputStream is = response.getEntity().getContent();
		if (is != null) streamToFile(is);	
	}
	
	private void streamToFile(InputStream is) throws Exception {
		File dir = Environment.getExternalStorageDirectory();
		File file = new File(dir, Constant.UPDATE_SAVENAME);
		FileOutputStream fos = new FileOutputStream(file);
		byte[] buf = new byte[1024];
		int ch = -1;
		while ((ch = is.read(buf)) != -1) {
			fos.write(buf, 0, ch);
		}
		if (fos != null) {
			fos.flush();
			fos.close();
		}
	}

	private void doUpdate() {
		pBar.cancel();
		handler.post(new Runnable() {
			@Override
			public void run() {
				update();				
			}
		});
	}

	private void update() {
		Intent it = new Intent(Intent.ACTION_VIEW);
		File dir = Environment.getExternalStorageDirectory();
		Uri data = Uri.fromFile(new File(dir, Constant.UPDATE_SAVENAME));
		it.setDataAndType(data, "application/vnd.android.package-archive");
		context.startActivity(it);
	}
	
	private int getLocalVerCode(Context cx) {
		try {
			PackageManager pm = cx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo("com.ssw.seller", 0); 
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	private String getLocalVerName(Context cx) {
		try {
			PackageManager pm = cx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo("com.ssw.seller", 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
}