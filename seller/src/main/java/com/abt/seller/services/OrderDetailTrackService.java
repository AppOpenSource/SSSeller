package com.abt.seller.services;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;
import com.abt.seller.models.Constant;
import com.abt.seller.models.OrderTrackDetail;
import com.abt.seller.utils.HttpUtil;

public class OrderDetailTrackService extends BaseHttpService {
	private List<OrderTrackDetail> list = new ArrayList<OrderTrackDetail>();

	public OrderDetailTrackService(Context context) {
		super(context);
	}

	public List<OrderTrackDetail> getOrderDetail(int orderId) {
		Log.d("TAG", "orderId : " + orderId);
		try {
			String res = HttpUtil.doGet(Constant.orderTrackUri + "/orderid/" + orderId);
//			String res = HttpUtil.doGet(Constant.orderTrackUri + "/orderid/" + 2);
//					"[[\"order_id\"," + orderId + "]]");
			return handleRes(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private List<OrderTrackDetail> handleRes(String res) throws JSONException {
		Log.d("TAG", "OrderTrackDetail res : " + res);
		JSONObject jo = new JSONObject(res);
		if (jo.getInt("res") == 0) {
			JSONArray jsa = jo.getJSONArray("data");
			for (int i = 0; i < jsa.length(); i++) {
				JSONObject dObj = jsa.getJSONObject(i);
				OrderTrackDetail detail = new OrderTrackDetail(
					dObj.getInt("orderid"), dObj.getString("content"),
					dObj.getString("people"), dObj.getString("addtime"));
//					"", dObj.getString("addtime"));
				Log.d("TAG", "OrderTrackDetail content : " + dObj.getString("content"));
				list.add(detail);
			}
		}
		return list;
	}
}