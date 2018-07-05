package com.abt.seller.activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

public class MsgReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context cx, Intent in) {
		// TODO Auto-generated method stub
		printLogs(in, in.getExtras());
		notificationOpened(in, cx, in.getExtras());
	}

	private void notificationOpened(Intent it, Context ct, Bundle bd) {
		if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(it.getAction())) {
			Intent in = new Intent(ct, MsgActivity.class);
			in.putExtras(bd);
			in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			ct.startActivity(in);
		}
	}

	private void printLogs(Intent it, Bundle bd) {
		Log.d("TAG", "onReceive - " + it.getAction());
		Log.d("TAG", "extras: " + printBundle(bd));
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(it.getAction())) {
			Log.d("TAG", "接收Registration Id : " + bd.getString(JPushInterface.EXTRA_REGISTRATION_ID));
		} else if (JPushInterface.ACTION_UNREGISTER.equals(it.getAction())) {
			Log.d("TAG", "接收UnRegistration Id : " + bd.getString(JPushInterface.EXTRA_REGISTRATION_ID));
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(it.getAction())) {
			Log.d("TAG", "接收到推送下来的自定义消息: " + bd.getString(JPushInterface.EXTRA_MESSAGE));
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(it.getAction())) {
			Log.d("TAG", "接收到推送下来的通知的ID: " + bd.getInt(JPushInterface.EXTRA_NOTIFICATION_ID));
		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(it.getAction())) {
			Log.d("TAG", "用户收到到RICH PUSH CALLBACK: " + bd.getString(JPushInterface.EXTRA_EXTRA));
		}
	}

	private String printBundle(Bundle bd) {
		StringBuilder sb = new StringBuilder();
		for (String key : bd.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bd.getInt(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bd.getString(key));
			}
		}
		return sb.toString();
	}
}