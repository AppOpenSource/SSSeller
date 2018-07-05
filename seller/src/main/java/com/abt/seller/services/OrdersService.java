package com.abt.seller.services;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;

import com.abt.seller.models.Constant;
import com.abt.seller.models.Login;
import com.abt.seller.models.Order;
import com.abt.seller.models.OrderGood;
import com.abt.seller.utils.HttpUtil;

public class OrdersService extends BaseHttpService {
	private List<Order> orders = new ArrayList<Order>();
	private List<OrderGood> orderGoodList = new ArrayList<OrderGood>();

	public OrdersService(Context context) {
		super(context);
	}

	public List<Order> getOrder(int type) {
		Log.d("TAG", "getOrder");
		try {
			orders.clear();
			LoginManager lm = LoginManager.getInstance(context);
			Login login = lm.getLoginInstance();
//			if (login != null && login.getAdminId() != -1) {
			if (true) {
//				JSONObject parms = new JSONObject();
//				parms.put("type", type);
//				parms.put("admin_id", login.getAdminId());
//				String res = HttpUtil.doGet(Constant.orderListUri,
//						"[[\"parm\"," + parms.toString() + "]]");
				// shopsid/1/status/5/p/1
				String res;
				switch (type) {
				case 0:
					res = HttpUtil.doGet(Constant.orderListUri + "/shopsid/1");
					return handleRes(res);
				case 1:
					res = HttpUtil.doGet(Constant.orderListUri + "/shopsid/1/status/6" );
					return handleRes(res);
				}
			}
		} catch (Exception e) {
			Log.d("TAG", e.toString());
			e.printStackTrace();
		}
		Log.d("TAG", "" + orders.toString());
		return orders;
	}

	private List<Order> handleRes(String res) throws JSONException {
		Log.d("TAG", "res : " + res);
		JSONObject jo = new JSONObject(res);
		JSONArray oa = (JSONArray) jo.get("data");
		for (int i = 0; i < oa.length(); i++) {
			JSONObject oo = oa.getJSONObject(i);
			JSONArray gja = oo.getJSONArray("goods");
			for (int j = 0; j < gja.length(); j++) {
				JSONObject go = gja.getJSONObject(j);
//				go.getInt("id")
				OrderGood order = new OrderGood(
						go.getInt("id"),
						go.getInt("shopsid"),
						go.getString("goods"),
						go.getString("picname"),
						go.getString("picpath"),
						go.getString("content"));
				orderGoodList.add(order);
			}
			Order order = new Order(
					oo.getInt("id"),
					oo.getString("ordernum"),
					oo.getInt("shopsid"),
					oo.getInt("uid"),
					oo.getString("linkman"),
					oo.getString("address"),
					oo.getString("phone"),
					(float) oo.getDouble("total"),
					oo.getInt("status"),
					oo.getString("addtime"),
					orderGoodList);
			orders.add(order);
		}
		return orders;
	}
}