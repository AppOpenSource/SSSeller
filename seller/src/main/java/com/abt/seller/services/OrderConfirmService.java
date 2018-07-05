package com.abt.seller.services;

import org.json.JSONObject;
import android.content.Context;
import android.util.Log;

import com.abt.seller.models.Constant;
import com.abt.seller.utils.HttpUtil;

public class OrderConfirmService extends BaseHttpService {

	public OrderConfirmService(Context context) {
		super(context);
	}

	public int getOrderConfirm(int orderId, int shopsid, int status, int preTime, int arrTime) {
		try {
//			StringBuilder sb = new StringBuilder();
//			sb.append(Constant.orderUploadUri);
//			sb.append("/order_id/");
//			sb.append(orderId);
//			sb.append("/letime/");
//			sb.append(time);
//			String res = HttpUtil.doGet(sb.toString());
			
			JSONObject jo = new JSONObject();
			jo.put("id", orderId);
			jo.put("shopsid", shopsid);
			jo.put("status", status);
			jo.put("astocktime", preTime);
			jo.put("aservicetime", arrTime);
			String res = HttpUtil.doPost(Constant.orderUploadUri, "[[\"parm\"," + jo.toString() + "]]");
			Log.d("TAG", "ggggggggggggggggggggggg res : " + res);
			JSONObject jso = new JSONObject(res);
			if (jso.optInt("res") == 0) {
				return jso.optInt("status");
			} else {
				return -1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}