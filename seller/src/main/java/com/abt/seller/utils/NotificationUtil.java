package com.abt.seller.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationUtil {
	public static final int STORAGE_ID = 2;
	public static final int QUOTA_ID = 3;
	public static final int AUTH_ERROR_ID = 4;
	public static final int UPLOAD_SUCCESS_ID = 10;
	public static final int UPLOAD_FAILED_ID = 11;
	public static final int UPDATE = 12; 
	public static final int MESSAGE = 13;
	public static final int CHARTMESSAGE = 14;

	@SuppressWarnings("deprecation")
	public static void notifyStatus(Context cx, int id, CharSequence title, 
			CharSequence content, Intent intent, int icon) {
		NotificationManager nm = (NotificationManager) cx
				.getSystemService("notification");
		PendingIntent pIntent = null;
		if (intent != null) {
			pIntent = PendingIntent.getActivity(cx, 0, intent, 0);
		} else {
			pIntent = PendingIntent.getBroadcast(cx, 0, new Intent(
					"com.facehand.action.DUMMY_ACTION"), 0);
		}
		Notification notify = new Notification(icon, title,
				System.currentTimeMillis());
		notify.ledARGB = -16711936;
		notify.ledOnMS = 300;
		notify.ledOffMS = 1000;
		notify.flags = Notification.DEFAULT_SOUND | notify.flags;
		notify.flags = Notification.FLAG_AUTO_CANCEL | notify.flags;
		notify.setLatestEventInfo(cx, title, content, pIntent);
		nm.notify(id, notify);
	}

	public static void cancel(Context cx, int id) {
		NotificationManager nm = (NotificationManager) cx
				.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(id);
	}
	
	public static void cancelAll(Context cx) {
		NotificationManager nm = (NotificationManager) cx
				.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancelAll();
	}
}